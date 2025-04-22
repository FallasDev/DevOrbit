package com.devorbit.app.service;

import com.devorbit.app.entity.*;
import com.devorbit.app.repository.RepositoryInscription;
import com.devorbit.app.repository.RepositoryPayment;
import lombok.AllArgsConstructor;

import com.devorbit.app.repository.CourseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InscriptionService {

    private final RepositoryInscription repositoryInscription;
    private final RepositoryPayment repositoryPayment;
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

    @Transactional
    public Inscription add(Inscription inscription) {
      
        Course course = inscription.getCourse();
        User user = inscription.getUser();
        
    
        Course existingCourse = repositoryCourse.findById(course.getId_course())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        
        if(!existingCourse.isStatus()) {
            throw new RuntimeException("El curso no está disponible para inscripción");
        }
        
   
        if(repositoryInscription.existsByUserAndCourse(user, course)) {
            throw new RuntimeException("El usuario ya está inscrito en este curso");
        }
        
      
        validatePayment(user, existingCourse.getPrice());
        
        
        if(inscription.getCreateAt() == null) {
            inscription.setCreateAt(LocalDateTime.now());
        }
        
       
        if(inscription.getProgress() == 0) {
            inscription.setProgress(0);
        }
        
        return repositoryInscription.save(inscription);
    }

 
    @Transactional
    public Inscription update(int id, Inscription inscription) {
        Inscription existing = repositoryInscription.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        
        existing.setProgress(inscription.getProgress());
        
        return repositoryInscription.save(existing);
    }

    private void validatePayment(User user, BigDecimal requiredAmount) {
        List<Payment> userPayments = repositoryPayment.findByUser(user);

        boolean hasValidPayment = userPayments.stream()
                .anyMatch(p ->
                        p.getTotal() >= requiredAmount.doubleValue() &&
                        p.getInscription() == null);

        if (!hasValidPayment) {
            throw new RuntimeException("El usuario no tiene un pago válido para esta inscripción");
        }
    }
}