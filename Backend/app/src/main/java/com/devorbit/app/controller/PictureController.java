package com.devorbit.app.controller;

import java.io.File;
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
//import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import com.devorbit.app.entity.Picture;
import com.devorbit.app.service.CloudinaryService;
import com.devorbit.app.service.PictureService;

import java.io.IOException; 

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/pictures")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Picture> getAllPictures() {
        return pictureService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Picture> getPictureById(@PathVariable int id) {
        return pictureService.findById(id)
                .map(picture -> ResponseEntity.ok(picture))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Picture> updatePicture(@PathVariable int id, @RequestBody Picture picture) {
        Picture updatedPicture = pictureService.update(id, picture);
        if (updatedPicture != null) {
            return ResponseEntity.ok(updatedPicture);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePicture(@PathVariable int id) {
        pictureService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadPicture(@RequestParam MultipartFile imageFile,
            @RequestParam int id_course) throws java.io.IOException {

        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + imageFile.getOriginalFilename());

        try {
            imageFile.transferTo(tempFile);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("File upload failed");
        }

        String publicId = UUID.randomUUID().toString();

        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadImage(tempFile.getAbsolutePath(), publicId);
            if (uploadResult != null) {
                String url = (String) uploadResult.get("secure_url");

                Picture savedPicture = pictureService.savePicture(uploadResult, id_course);

                if (savedPicture != null) {
                    return ResponseEntity.ok(savedPicture); 
                } else {
                    throw new Exception("Failed to save picture to database");
                }
            }
            throw new Exception("Upload to Cloudinary failed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @GetMapping("/user/data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> checkUserAdmin() {
        return ResponseEntity.ok(true);
    }
}