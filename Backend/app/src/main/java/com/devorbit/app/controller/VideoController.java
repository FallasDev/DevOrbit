package com.devorbit.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devorbit.app.entity.Video;
import com.devorbit.app.service.VideoService;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public Video getVideoById(int id) {
        return videoService.getVideoById(id);
    }

    @PostMapping
    public Video saveVideo(Video video) {
        return videoService.saveVideo(video);
    }

    @PostMapping("/{id}")
    public Video updateVideo(int id, Video video) {
        return videoService.updateVideo(id, video);
    }

    @PostMapping("/{id}/delete")
    public void deleteVideo(int id) {
        videoService.deleteVideo(id);
    }

}
