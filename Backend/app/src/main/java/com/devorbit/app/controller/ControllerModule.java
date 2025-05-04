package com.devorbit.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devorbit.app.entity.Module;
import com.devorbit.app.service.ModuleService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@CrossOrigin(origins = "*") // Permitir acceso desde cualquier origen
// Tag(name = "Modules", description = "API para gestionar modulos") // Grupo en
// Swagger

@RestController
@RequestMapping("/api/modules")
public class ControllerModule {

    @Autowired
    private ModuleService moduleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Module> getAllModules() {
        return moduleService.findAll();
    }

    @GetMapping("/course/{courseId}") // optener cursos por curso, pero aun no tengo el metodo en service, es lo miso
    public List<Module> getModulesByCurse(@PathVariable int courseId) {
        return moduleService.findByCourseId(courseId);
    }

    @GetMapping("/{id}") // optener cursoooo
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Optional<Module> getModuleById(@PathVariable int id) {
        return moduleService.findById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Module createModule(
            @RequestParam String title,
            @RequestParam String descripcion,
            @RequestParam int courseId,
            @RequestParam List<Integer> moduleOrder) {

        return moduleService.save(title, descripcion, courseId, moduleOrder);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Module updateModule(
            @PathVariable int id,
            @RequestParam String title,
            @RequestParam String descripcion,
            @RequestParam int courseId,
            @RequestParam List<Integer> moduleOrder) {
                
        return moduleService.update(id,title, descripcion, courseId, moduleOrder);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteModule(@PathVariable int id) {
        moduleService.deleteById(id);
    }

}
