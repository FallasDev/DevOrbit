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

        inscription.setUser(user);
        inscription.setCourse(course);
        inscription.setProgress(0);

        return repositoryInscription.save(inscription);
    }

    public Inscription getByUserAndCourse(int userId, int courseId) {

        User user = repositoryUser.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Course course = repositoryCourse.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        return repositoryInscription.findByUserAndCourse(user, course).orElse(null);
    }

    public List<Inscription> getUseInscriptions(int userId) {
        User user = repositoryUser.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return repositoryInscription.findByUser(user);
    }

    @Transactional
    public Inscription update(int id, Inscription inscription) {
        Inscription existing = repositoryInscription.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        existing.setProgress(inscription.getProgress());

        return repositoryInscription.save(existing);
    }
}
