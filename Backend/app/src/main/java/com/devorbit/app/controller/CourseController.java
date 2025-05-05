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

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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


    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Course> getAllCurses() {
        List<Course> courses = courseService.findAll();
        return courses;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Optional<Course> getCurseById(@PathVariable int id) {
        return courseService.findById(id);
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

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Course> createCourse(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam boolean status,
            @RequestParam(value = "picture", required = true) MultipartFile pictureFile) {

        Picture picture = null;

        // Subir imagen si fue proporcionada
        if (pictureFile != null && !pictureFile.isEmpty()) {
            File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + pictureFile.getOriginalFilename());
            try {
                pictureFile.transferTo(tempFile);

                String publicId = UUID.randomUUID().toString();
                Map<String, Object> uploadResult = cloudinaryService.uploadImage(tempFile.getAbsolutePath(), publicId);

                String url = (String) uploadResult.get("secure_url");

                picture = new Picture();
                picture.setUrl(url);

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).build();
            } finally {
                if (tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }

        // Crear el curso
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setPrice(price);
        course.setStatus(status);
        if (picture != null) {
            course.setPicture(picture);
        }

        Course savedCourse = courseService.save(course);
        return ResponseEntity.ok(savedCourse);
    }
}    


