package minjae.academy.subject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/subject")
public class SubjectController {
    @GetMapping
    public String subject(){
        return "admin/subject/subject";
    }
}
