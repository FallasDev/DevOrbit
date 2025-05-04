package com.devorbit.app.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import com.devorbit.app.entity.Picture;
import com.devorbit.app.service.CloudinaryService;
import com.devorbit.app.service.CourseService;
import com.devorbit.app.service.PictureService;

import io.swagger.v3.oas.models.media.MediaType;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*") // Permitir acceso desde cualquier origen
// @Tag(name = "Curses", description = "API para gestionar cursos") // Grupo en
// Swagger

@RestController
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
        return courseService.save(updatedCourse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletedCourse(@PathVariable int id) {
        courseService.deleteById(id);
    }

    @PutMapping("/{id}/status") // estado de activo o no
    public Course updateStatus(@PathVariable int id, @RequestParam boolean status) {
        Course course = courseService.findById(id).orElseThrow();
        course.setStatus(status);
        return courseService.save(course);
    }

    /* 
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> createCourse(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("videoUrl") String videoUrl,
            @RequestParam("status") boolean status,
            @RequestParam("picture") MultipartFile pictureFile) throws Exception {

        String publicId = UUID.randomUUID().toString();
        Map<String, Object> uploadResult = cloudinaryService.uploadImage(
                pictureFile.getBytes(), publicId);
        String secureUrl = uploadResult.get("secure_url").toString();

        Picture picture = new Picture();
        picture.setUrl(secureUrl);
        picture = pictureService.save(picture);

        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setPrice(price);
        course.setVideoUrl(videoUrl);
        course.setStatus(status);
        course.setPicture(picture);

        Course savedCourse = courseService.save(course);

        return ResponseEntity.ok(savedCourse);
    }*/

}
