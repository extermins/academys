package minjae.academy.video.controller;

import lombok.RequiredArgsConstructor;
import minjae.academy.braedcrumb.Breadcrumb;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/video")
@RequiredArgsConstructor
public class VideoController {
    private final Breadcrumb breadcrumb;

    @GetMapping
    public String video(Model model) {
        breadcrumb.addBreadcrumb(model,"video","동영상");
        return "admin/video/video";
    }
}
