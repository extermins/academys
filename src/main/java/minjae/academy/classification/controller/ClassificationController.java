package minjae.academy.classification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/classification")
public class ClassificationController {
    @GetMapping
    public String classification(){
        return "admin/classification/classification";
    }
}
