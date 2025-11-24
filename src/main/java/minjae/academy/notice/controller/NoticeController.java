package minjae.academy.notice.controller;

import lombok.RequiredArgsConstructor;
import minjae.academy.braedcrumb.Breadcrumb;
import minjae.academy.notice.dto.NoticeCreateDto;
import minjae.academy.notice.dto.NoticeUpdateDto;
import minjae.academy.notice.entity.Notice;
import minjae.academy.notice.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        breadcrumb.addBreadcrumb(model,"notice","공지사항");
        Notice noticeq = noticeService.findById(uuid).orElseThrow(() -> new RuntimeException("Notice not found"));
        model.addAttribute("notice",noticeq);
        return "/admin/notice/getOne";
    }

//    @GetMapping("/edit")
//    public String edit(@RequestParam String uuid,Model model){
//        breadcrumb.addBreadcrumb(model,"notice","공지사항");
//        Notice noticeq = noticeService.findById(uuid).orElseThrow(() -> new RuntimeException("Notice not found"));
//
//        model.addAttribute("noticeUpdateDto",noticeq);
//        return "/admin/notice/edit";
//    }

    @GetMapping("/edit")
    public String editForm(@RequestParam String uuid, Model model) {
        Notice notice = noticeService.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        // DTO 생성 및 값 설정
        NoticeUpdateDto noticeUpdateDto = new NoticeUpdateDto();
        noticeUpdateDto.setTitle(notice.getTitle());
        noticeUpdateDto.setContent(notice.getContent());

        // Model에 추가 - 이름을 정확히 맞춰야 함!
        model.addAttribute("noticeUpdateDto", noticeUpdateDto);
        model.addAttribute("uuid", uuid);

        return "admin/notice/edit";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam String uuid,
                       @ModelAttribute NoticeUpdateDto noticeUpdateDto,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes,
                       Model model){
        if(bindingResult.hasErrors()){
            return "admin/notice/edit";
        }

        Notice notice = noticeUpdateDto.toEntity();
        noticeService.updateNotice(uuid,notice);
        return "redirect:/admin/notice";
    }

    @PostMapping("/delete/{uuid}")
    public String delete(@PathVariable("uuid") String uuid){
        noticeService.deleteNotice(uuid);
        return "redirect:/admin/notice";
    }
}
