package minjae.academy.banner.controller;

import lombok.RequiredArgsConstructor;
import minjae.academy.banner.dto.BannerCreateDto;
import minjae.academy.banner.dto.BannerOrderDto;
import minjae.academy.banner.dto.BannerUpdateDto;
import minjae.academy.banner.entity.Banner;
import minjae.academy.banner.service.BannerService;
import minjae.academy.braedcrumb.Breadcrumb;
import minjae.academy.file.service.BannerFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;
    private final BannerFileService bannerFileService;
    private final Breadcrumb breadcrumb;

    @GetMapping
    public String Banner(Model model) {
        breadcrumb.addBreadcrumb(model,"banner","배너");
        List<Banner> banners = bannerService.allBanners();
        model.addAttribute("banners", banners);
        return "admin/banner/banner";
    }

    @GetMapping("/create")
    public String Create(@ModelAttribute BannerCreateDto bannerCreateDto,Model model) {
        breadcrumb.addBreadcrumb(model,"banner","배너");
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

    @GetMapping("/edit")
    public String edit(@RequestParam String uuid,Model model,@ModelAttribute BannerUpdateDto bannerUpdateDto) {
        Banner banner = bannerService.getOneBanner(uuid);
        BannerUpdateDto dto = BannerUpdateDto.builder()
                .uuid(banner.getUuid())
                .description(banner.getDescription())
                .linkUrl(banner.getLinkUrl())
                .imageUrl(banner.getImageUrl())
                .enabled(banner.isEnabled())
                .startDate(banner.getStartDate())
                .endDate(banner.getEndDate())
                .build();
        model.addAttribute("bannerUpdateDto",dto);
        return "admin/banner/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BannerUpdateDto bannerUpdateDto,
                         BindingResult bindingResult,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         RedirectAttributes redirectAttributes) throws IOException {

        if(bindingResult.hasErrors()){
            return "admin/banner/edit";
        }

        // 기존 배너 정보 조회 (기존 이미지 파일명을 가져오기 위해)
        Banner existingBanner = bannerService.getOneBanner(bannerUpdateDto.getUuid());
        String oldImageUrl = existingBanner.getImageUrl();

        // 새 이미지가 업로드된 경우
        if(imageFile != null && !imageFile.isEmpty()){
            // 새 이미지 저장
            String newImageUrl = bannerFileService.saveBannerImage(imageFile);

            // 기존 이미지 삭제
            if(oldImageUrl != null && !oldImageUrl.isEmpty()) {
                bannerFileService.deleteBannerImage(oldImageUrl);
            }

            // 새 이미지 URL 설정
            bannerUpdateDto.setImageUrl(newImageUrl);
        } else {
            // 이미지가 업로드되지 않은 경우 기존 이미지 URL 유지
            bannerUpdateDto.setImageUrl(oldImageUrl);
        }

        // 배너 업데이트
        bannerService.updateBanner(bannerUpdateDto);

        redirectAttributes.addFlashAttribute("message", "배너가 성공적으로 수정되었습니다.");
        return "redirect:/admin/banner";
    }
}
