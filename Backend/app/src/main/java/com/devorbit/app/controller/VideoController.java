package com.devorbit.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Video getVideoById(@PathVariable int id) {
        return videoService.getVideoById(id);
    }

    @PostMapping
    public Video saveVideo(@RequestBody Video video) {
        return videoService.saveVideo(video);
    }

    @PutMapping("/{id}")
    public Video updateVideo(@PathVariable int id,@RequestBody Video video) {
        return videoService.updateVideo(id, video);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteVideo(@PathVariable int id) {
        videoService.deleteVideo(id);
    }

}
ccc