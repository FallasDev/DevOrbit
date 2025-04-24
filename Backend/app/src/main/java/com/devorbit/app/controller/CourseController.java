package com.devorbit.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.devorbit.app.entity.Course;
import com.devorbit.app.service.CourseService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*") // Permitir acceso desde cualquier origen
//@Tag(name = "Curses", description = "API para gestionar cursos") // Grupo en Swagger

@RestController
@RequestMapping("/api/admin/curses") 
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Course> getAllCurses() {
        return courseService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") 
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

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')") 
    public Course updateStatus(@PathVariable int id, @RequestParam boolean status) {
        Course course = courseService.findById(id).orElseThrow();
        course.setStatus(status);
        return courseService.save(course);
    }

}
