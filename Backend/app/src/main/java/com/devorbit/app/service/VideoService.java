package com.devorbit.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Video;
import com.devorbit.app.repository.ModuleRepository;
import com.devorbit.app.repository.VideoRepository;

import com.devorbit.app.entity.Module;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VideoService {
    
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ModuleRepository moduleRepository;
    
    public Video saveVideo(Map<String, Object> uploadResult, String title, int idModule, List<Integer> videoOrder) {

        Module module = moduleRepository.findById(idModule).orElse(null);

        if (module == null) {
            return null; 
        }

        // Me llega una lista de ids de videos con el orden que quiere el usuario y debo ordenar los videos con ese orden
        
        int ID_VIDEO_NUEVO = 0;
        Video video = new Video();
        video.setTitle(title);
        video.setUrl(uploadResult.get("url").toString());
        video.setModule(module);

        for (int i = 0; i < videoOrder.size(); i++) {

            int nuevo_orden = i + 1;

            if (videoOrder.get(i) == ID_VIDEO_NUEVO){
                video.setVideoOrder(nuevo_orden);
            }

            Video nowVideo = videoRepository.findById(videoOrder.get(i)).orElse(null);
            if (nowVideo == null) continue;
            nowVideo.setVideoOrder(nuevo_orden);
        }
    
        double duration = Double.parseDouble(uploadResult.get("duration").toString());
        int duration_parsed = (int) Math.round(duration); 

        video.setDuration_seg(duration_parsed);
        
        try{
            return videoRepository.save(video);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public List<Video> getVideosByModule(int idModule){
        return videoRepository.findByIdModule(idModule);
    }

}
