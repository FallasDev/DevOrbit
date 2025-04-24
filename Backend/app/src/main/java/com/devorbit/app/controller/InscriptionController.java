package com.devorbit.app.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.devorbit.app.entity.*;
import com.devorbit.app.service.InscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@Tag(name = "Inscriptions", description = "API para gestionar inscripciones de cursos con validación de pagos")
@RestController
@RequestMapping("/api/inscriptions")
public class InscriptionController {
    
    private final InscriptionService inscriptionService;

    
    public InscriptionController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener todas las inscripciones")
    public ResponseEntity<List<Inscription>> getAllInscriptions() {
        return ResponseEntity.ok(inscriptionService.get());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener inscripción por ID")
    public ResponseEntity<Inscription> getInscriptionById(@PathVariable int id) {
        return inscriptionService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Crear nueva inscripción con validación de pago")
    public ResponseEntity<?> createInscription(@RequestBody InscriptionRequest request) {
        try {
            Inscription createdInscription = inscriptionService.add(request.toInscription());
            return ResponseEntity.ok(createdInscription);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/progress")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Actualizar progreso de inscripción")
    public ResponseEntity<Inscription> updateProgress(
            @PathVariable int id, 
            @RequestParam int progress) {
        Inscription inscription = new Inscription();
        inscription.setProgress(progress);
        return ResponseEntity.ok(inscriptionService.update(id, inscription));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Eliminar inscripción")
    public ResponseEntity<Void> deleteInscription(@PathVariable int id) {
        inscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    
    public static class InscriptionRequest {
        private User user;
        private Course course;
        
        
        public Inscription toInscription() {
            Inscription inscription = new Inscription();
            inscription.setUser(this.user);
            inscription.setCourse(this.course);
            return inscription;
        }
    }
}