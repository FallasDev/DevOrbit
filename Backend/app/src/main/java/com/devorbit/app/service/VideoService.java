package com.devorbit.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Video;
import com.devorbit.app.repository.VideoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VideoService {
    
    @Autowired
    private VideoRepository videoRepository;
    
    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    public Video getVideoById(int id) {
        return videoRepository.findById(id).orElse(null);
    }

    public void deleteVideo(int id) {
        videoRepository.deleteById(id);
    }

    public Video updateVideo(int id, Video video) {
        if (videoRepository.existsById(id)) {
            video.setVideo_id(id);
            return videoRepository.save(video);
        }
        return null;
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

}
