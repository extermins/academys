package minjae.academy.classification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/classification")
public class ClassificationController {

    @GetMapping
    public String classification(){
        return "admin/classification/classification";
    }

    @GetMapping("/")
    public String create(Model model){
        return "admin/classification/create";
    }
}
