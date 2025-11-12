package minjae.academy.notice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/notice")
public class NoticeController {

    @GetMapping
    public String notice(){
        return "admin/notice/notice";
    }
}
