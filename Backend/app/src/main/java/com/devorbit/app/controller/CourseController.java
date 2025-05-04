package com.devorbit.app.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.devorbit.app.entity.Course;
import com.devorbit.app.service.CloudinaryService;
import com.devorbit.app.service.CourseService;
import com.devorbit.app.service.PictureService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @Tag(name = "Curses", description = "API para gestionar cursos") // Grupo en
// Swagger

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private PictureService pictureService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Course> getAllCurses() {
        return courseService.findAll();
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Optional<Course> getCurseById(@PathVariable int id) {
        return courseService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Course createCourse(@RequestBody Course course) {
        return courseService.save(course);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Course updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
        updatedCourse.setId_course(id);
        return courseService.update(id,updatedCourse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletedCourse(@PathVariable int id) {
        courseService.deleteById(id);
    }

    @PutMapping("/{id}/status")//estado de activo o no
    public Course updateStatus(@PathVariable int id, @RequestParam boolean status) {
        Course course = courseService.findById(id).orElseThrow();
        course.setStatus(status);
        return courseService.save(course);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadPicture(@RequestParam MultipartFile videoFile, @RequestParam String title,@RequestParam int idModule, @RequestParam List<Integer> videoOrder) {

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
            Map<String, Object> uploadResult = cloudinaryService.uploadImage(tempFile.getAbsolutePath(), publicId);
            if (uploadResult != null) {
                pictureService.savePicture(uploadResult);
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

}
