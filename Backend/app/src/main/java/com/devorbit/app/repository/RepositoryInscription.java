package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devorbit.app.entity.Course;
import com.devorbit.app.entity.Inscription;
import com.devorbit.app.entity.User;

public interface RepositoryInscription extends JpaRepository<Inscription, Integer> {

    boolean existsByUserAndCourse(User user, Course course);
    
}
