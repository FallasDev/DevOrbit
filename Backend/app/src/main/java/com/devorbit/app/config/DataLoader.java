package com.devorbit.app.config;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.devorbit.app.entity.User;
import com.devorbit.app.repository.RepositoryUser;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RepositoryUser repositoryUser; // Corregí el nombre para seguir convenciones

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        repositoryUser.findByUsername("admin").ifPresentOrElse(
            user -> {
                System.out.println("Usuario admin ya existe");
                if (needsRehash(user.getPassword())) {
                    user.setPassword(passwordEncoder.encode("admin"));
                    repositoryUser.save(user);
                }
            },
            () -> {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEmail("admins@example.com"); 
                admin.setCreatedAt(LocalDateTime.now()); 
                admin.setRole("ROLE_ADMIN");
                repositoryUser.save(admin);
                System.out.println("Usuario 'admin' creado");
            }
        );

        repositoryUser.findByUsername("user").ifPresentOrElse(
            user -> {
                System.out.println("Usuario user ya existe");
            },
            () -> {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user"));
                user.setEmail("user@example.com"); 
                user.setCreatedAt(LocalDateTime.now()); 
                user.setRole("ROLE_USER");
                repositoryUser.save(user);
                System.out.println("Usuario 'user' creado");
            }
        );
    }

    private boolean needsRehash(String encodedPassword) {
        return encodedPassword.contains(" ") || !passwordEncoder.upgradeEncoding(encodedPassword);
    }
}