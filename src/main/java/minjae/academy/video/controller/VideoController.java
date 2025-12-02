package minjae.academy.video.controller;

import lombok.RequiredArgsConstructor;
import minjae.academy.braedcrumb.Breadcrumb;
import minjae.academy.video.entity.Video;
import minjae.academy.video.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/video")
@RequiredArgsConstructor
public class VideoController {
    private final Breadcrumb breadcrumb;
    private final VideoService videoService;


    @GetMapping
    public String video(Model model) {
        breadcrumb.addBreadcrumb(model,"video","동영상");
        List<Video> videos = videoService.getAllVideos();
        model.addAttribute("videos", videos);
        return "admin/video/video";
    }

    @GetMapping("/create")
    public String create(Model model) {
        breadcrumb.addBreadcrumb(model,"video","동영상");
        return "admin/video/create";
    }

    @GetMapping("/{id}")
    public String videoDetail(@PathVariable String id, Model model) {
        Video video = videoService.getVideo(id);
        model.addAttribute("video", video);
        return "admin/video/detail";
    }
}
