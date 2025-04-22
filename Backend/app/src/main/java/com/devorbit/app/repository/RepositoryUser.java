package com.devorbit.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devorbit.app.entity.User;

public interface RepositoryUser extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    
} 
