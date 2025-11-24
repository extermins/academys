package minjae.academy.notice.controller;

import lombok.RequiredArgsConstructor;
import minjae.academy.braedcrumb.Breadcrumb;
import minjae.academy.notice.dto.NoticeCreateDto;
import minjae.academy.notice.entity.Notice;
import minjae.academy.notice.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticeController {
    private final Breadcrumb breadcrumb;
    private final NoticeService noticeService;

    @GetMapping
    public String notice(Model model){
        breadcrumb.addBreadcrumb(model,"notice","공지사항");
        List<Notice> notice = noticeService.findAll();
        model.addAttribute("notices",notice);
        return "admin/notice/notice";
    }

    @GetMapping("/create")
    public String create(Model model,
                         @ModelAttribute NoticeCreateDto noticeCreateDto){
        breadcrumb.addBreadcrumb(model,"notice","공지사항");
        return "admin/notice/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute NoticeCreateDto noticeCreateDto,
                         BindingResult bindingResult,
                         Model model) throws IOException {

        if(bindingResult.hasErrors()){
            return "admin/notice/create";
        }

        Notice notice = noticeCreateDto.toEntity();
        noticeService.addNotice(notice);

        return "redirect:/admin/notice";
    }

    @GetMapping("/{uuid}")
    public String getOne(@PathVariable("uuid") String uuid, Model model){
        Notice noticeq = noticeService.findById(uuid).orElseThrow(() -> new RuntimeException("Notice not found"));
        model.addAttribute("notice",noticeq);
        return "/admin/notice/edit";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam String uuid,Model model){
        Notice noticeq = noticeService.findById(uuid).orElseThrow(() -> new RuntimeException("Notice not found"));
        model.addAttribute("notice",noticeq);
        return "/admin/notice/edit";
    }
}
