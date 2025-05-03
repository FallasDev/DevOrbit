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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Obtener todas las inscripciones")
    public ResponseEntity<List<Inscription>> getAllInscriptions() {
        return ResponseEntity.ok(inscriptionService.get());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Obtener inscripción por ID")
    public ResponseEntity<Inscription> getInscriptionById(@PathVariable int id) {
        return inscriptionService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Actualizar progreso de inscripción")
    public ResponseEntity<Inscription> updateProgress(
            @PathVariable int id,
            @RequestParam int progress) {
        Inscription inscription = new Inscription();
        inscription.setProgress(progress);
        return ResponseEntity.ok(inscriptionService.update(id, inscription));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Eliminar inscripción")
    public ResponseEntity<Void> deleteInscription(@PathVariable int id) {
        inscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public static class InscriptionRequest {
        private int userId;
        private int courseId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public Inscription toInscription() {
            User user = new User();
            user.setIdUser(this.userId);

            Course course = new Course();
            course.setId_course(this.courseId);

            Inscription inscription = new Inscription();
            inscription.setUser(user);
            inscription.setCourse(course);
            return inscription;
        }
    }

}