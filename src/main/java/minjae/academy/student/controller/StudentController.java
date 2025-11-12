package minjae.academy.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/student")
public class StudentController {
    @GetMapping
    public String Member(){
        return "admin/student/student";
    }
}
