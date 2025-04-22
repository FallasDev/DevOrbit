package com.devorbit.app.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devorbit.app.entity.Course;
import com.devorbit.app.service.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Courses", description = "API para gestionar cursos")
@RestController
@RequestMapping("/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;

    @GetMapping
    @Operation(summary = "Obtener todos los cursos")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.get());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener curso por ID")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        Optional<Course> course = courseService.getById(id);
        return course.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo curso")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.add(course));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar curso")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course course) {
        return ResponseEntity.ok(courseService.update(id, course));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar curso")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}