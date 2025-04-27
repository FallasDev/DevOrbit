package com.devorbit.app.service;

import com.devorbit.app.entity.*;
import com.devorbit.app.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InscriptionService {

    private final RepositoryInscription repositoryInscription;
    private final RepositoryUser repositoryUser; 
    private final CourseRepository repositoryCourse;
    private final PaymentService paymentService;

    public List<Inscription> get() {
        return repositoryInscription.findAll();
    }

    public Optional<Inscription> getById(int id) {
        return repositoryInscription.findById(id);
    }

    public void delete(int id) {
        repositoryInscription.deleteById(id);
    }

    public Inscription add(Inscription inscription) {
        User user = repositoryUser.findById(inscription.getUser().getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Course course = repositoryCourse.findById(inscription.getCourse().getId_course())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        boolean pagoValido = paymentService.get()
                .stream()
                .anyMatch(p -> p.getUser().getIdUser() == user.getIdUser() &&
                        p.getTotal().compareTo(course.getPrice()) >= 0);

        if (!pagoValido) {
            throw new RuntimeException("No se encontró un pago válido para este curso.");
        }

        inscription.setUser(user);
        inscription.setCourse(course);
        inscription.setProgress(0);

        return repositoryInscription.save(inscription);
    }

    @Transactional
    public Inscription update(int id, Inscription inscription) {
        Inscription existing = repositoryInscription.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        existing.setProgress(inscription.getProgress());

        return repositoryInscription.save(existing);
    }
}
