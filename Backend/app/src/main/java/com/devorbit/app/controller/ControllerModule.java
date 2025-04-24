package com.devorbit.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devorbit.app.entity.Module;
import com.devorbit.app.service.ModuleService;

@CrossOrigin(origins = "*") // Permitir acceso desde cualquier origen
//Tag(name = "Modules", description = "API para gestionar modulos") // Grupo en Swagger

@RestController
@RequestMapping("/modules")
public class ControllerModule {

    @Autowired
    private ModuleService moduleService;

    @GetMapping
    public List<Module> getAllModules(){
        return moduleService.findAll();
    }

    /**@GetMapping("/curse/{curseId}")//optener cursos por curso, pero aun no tengo el metodo en service, es lo miso
    public List<Module> getModulesByCurse(@PathVariable int curseId) {
        return moduleService.findById(curseId);
    }*/

    @GetMapping("/{id}")//optener cursoooo
    public Optional<Module> getModuleById(@PathVariable int id) {
        return moduleService.findById(id);
    }

    @PostMapping
    public Module createModule(@RequestBody Module module) {
        return moduleService.save(module);
    }

    @PutMapping("/{id}")
    public Module updateModule(@PathVariable int id, @RequestBody Module updatedModule) {
        updatedModule.setId_module(id);
        return moduleService.save(updatedModule);
    }

    @DeleteMapping("/{id}")
    public void deleteModule(@PathVariable int id) {
        moduleService.deleteById(id);
    }

}
