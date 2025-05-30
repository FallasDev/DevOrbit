package com.devorbit.app.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devorbit.app.entity.Video;
import com.devorbit.app.service.CloudinaryService;
import com.devorbit.app.service.VideoService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Video getVideoById(@PathVariable int id) {
        return videoService.getVideoById(id);
    }

    @GetMapping("/module/{idModule}")
    public List<Video> getVideosByModule(@PathVariable int idModule){
        return videoService.getVideosByModule(idModule);
    } 

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Video updateVideo(@PathVariable int id, @RequestBody Video video) {
        return videoService.updateVideo(id, video);
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteVideo(@PathVariable int id) {
        videoService.deleteVideo(id);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadVideo(@RequestParam MultipartFile videoFile, @RequestParam String title,@RequestParam int idModule, @RequestParam List<Integer> videoOrder) {

        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + videoFile.getOriginalFilename());

        try {
            videoFile.transferTo(tempFile);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("File upload failed");
        }

        System.out.println(videoOrder);
        String publicId = UUID.randomUUID().toString();
        
        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadVideo(tempFile.getAbsolutePath(), publicId);
            if (uploadResult != null) {
                videoService.saveVideo(uploadResult, title, idModule, videoOrder);
                return ResponseEntity.ok(uploadResult);
            }
            throw new Exception("Upload failed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed");
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }

    }

    @GetMapping("/user/data")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean checkUserAdmin() {
        return true;
    }



}
