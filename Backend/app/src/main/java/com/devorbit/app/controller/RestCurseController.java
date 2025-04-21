package com.devorbit.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.devorbit.app.emtity.Curse;
import com.devorbit.app.service.CurseService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*") // Permitir acceso desde cualquier origen
//@Tag(name = "Curses", description = "API para gestionar cursos") // Grupo en Swagger

@RestController
@RequestMapping("/curses")
public class RestCurseController {

    @Autowired
    private CurseService curseService;

    @GetMapping
    public List<Curse> getAllCurses() {
        return curseService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Curse> getCurseById(@PathVariable int id) {
        return curseService.findById(id);
    }

    @PutMapping("/{id}")
    public Curse createCurse(@RequestBody Curse curse) {
        return curseService.save(curse);
    }

    @PutMapping("/{id}")
    public Curse updateCurse(@PathVariable int id, @RequestBody Curse updatedCurse) {
        updatedCurse.setId_curse(id);
        return curseService.save(updatedCurse);
    }

    @DeleteMapping("/{id}")
    public void deletedCurse(@PathVariable int id) {
        curseService.deleteById(id);
    }

    @PutMapping("/{id}/status")//estado de activo o no
    public Curse updateStatus(@PathVariable int id, @RequestParam boolean status) {
        Curse curse = curseService.findById(id).orElseThrow();
        curse.setStatus(status);
        return curseService.save(curse);
    }

}
