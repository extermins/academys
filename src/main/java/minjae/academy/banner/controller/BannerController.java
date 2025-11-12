package minjae.academy.banner.controller;

import lombok.RequiredArgsConstructor;
import minjae.academy.banner.dto.BannerCreateDto;
import minjae.academy.banner.dto.BannerOrderDto;
import minjae.academy.banner.entity.Banner;
import minjae.academy.banner.service.BannerService;
import minjae.academy.file.service.BannerFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;
    private final BannerFileService bannerFileService;

    @GetMapping
    public String Banner(Model model) {
        List<Banner> banners = bannerService.allBanners();
        model.addAttribute("banners", banners);
        return "admin/banner/banner";
    }

    @GetMapping("/create")
    public String Create(@ModelAttribute BannerCreateDto bannerCreateDto){
        return "admin/banner/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute BannerCreateDto bannerCreateDto,
                         BindingResult bindingResult,
                         @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        if(bindingResult.hasErrors()){
            return "admin/banner/create";
        }
        if(imageFile.isEmpty()){
            return "admin/banner/create";
        }
        String imageUrl = bannerFileService.saveBannerImage(imageFile);

        Banner banner = bannerCreateDto.toBanner();
        banner.setImageUrl(imageUrl);
        bannerService.addBanner(banner);

        return "/admin/banner/create";
    }

//    @GetMapping
//    public String edit(@RequestParam("uuid") UUID id){
//       return "admin/banner/edit";
//    }
}
