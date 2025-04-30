package com.devorbit.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService curseService;

    @GetMapping
    public List<Course> getAllCurses() {
        return curseService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Course> getCurseById(@PathVariable int id) {
        return curseService.findById(id);
    }

    @PostMapping
    public Course createCurse(@RequestBody Course curse) {
        return curseService.save(curse);
    }

    @PutMapping("/{id}")
    public Course updateCurse(@PathVariable int id, @RequestBody Course updatedCurse) {
        updatedCurse.setId_course(id);
        return curseService.save(updatedCurse);
    }

    @DeleteMapping("/{id}")
    public void deletedCurse(@PathVariable int id) {
        curseService.deleteById(id);
    }

    @PutMapping("/{id}/status")//estado de activo o no
    public Course updateStatus(@PathVariable int id, @RequestParam boolean status) {
        Course curse = curseService.findById(id).orElseThrow();
        curse.setStatus(status);
        return curseService.save(curse);
    }

}
