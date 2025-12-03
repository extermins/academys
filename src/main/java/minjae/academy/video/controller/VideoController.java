package minjae.academy.video.controller;

import lombok.RequiredArgsConstructor;
import minjae.academy.braedcrumb.Breadcrumb;
import minjae.academy.video.entity.Video;
import minjae.academy.video.service.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/video")
@RequiredArgsConstructor
public class VideoController {
    private final Breadcrumb breadcrumb;
    private final VideoService videoService;

    @GetMapping
    public String video(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            Model model) {

        breadcrumb.addBreadcrumb(model, "video", "동영상");

        // 페이지 크기 제한 (최대 100)
        size = Math.min(size, 100);

        Pageable pageable = PageRequest.of(page, size);

        Page<Video> videoPage;
        if ((keyword != null && !keyword.isEmpty()) || (status != null && !status.isEmpty())) {
            // 검색 조건이 있는 경우
            Video.VideoStatus videoStatus = null;
            if (status != null && !status.isEmpty()) {
                try {
                    videoStatus = Video.VideoStatus.valueOf(status);
                } catch (IllegalArgumentException ignored) {
                }
            }
            videoPage = videoService.searchVideos(keyword, videoStatus, pageable);
        } else {
            // 전체 목록
            videoPage = videoService.getVideos(pageable);
        }

        model.addAttribute("videos", videoPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", videoPage.getTotalPages());
        model.addAttribute("totalElements", videoPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("hasPrevious", videoPage.hasPrevious());
        model.addAttribute("hasNext", videoPage.hasNext());

        return "admin/video/video";
    }

    @GetMapping("/create")
    public String create(Model model) {
        breadcrumb.addBreadcrumb(model, "video", "동영상");
        return "admin/video/create";
    }

    @GetMapping("/{id}")
    public String videoDetail(@PathVariable String id, Model model) {
        Video video = videoService.getVideo(id);
        model.addAttribute("video", video);
        return "admin/video/detail";
    }
}
