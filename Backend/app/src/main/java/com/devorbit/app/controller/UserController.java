package com.devorbit.app.controller;

import com.devorbit.app.entity.User;
import com.devorbit.app.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private RepositoryUser repositoryUser;

    @GetMapping("/admin/data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAdminData() {
        return ResponseEntity.ok("Información confidencial para ADMIN");
    }

    @GetMapping("/user/data")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getUserData() {
        return ResponseEntity.ok("Información exclusiva para USER");
    }

    @GetMapping("/all")
    public ResponseEntity<String> getPublicData() {
        return ResponseEntity.ok("Información accesible para todos los usuarios autenticados");
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = repositoryUser.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateCurrentUser(@RequestBody User updatedUser, Authentication authentication) {
        String username = authentication.getName();
        User user = repositoryUser.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setEmail(updatedUser.getEmail());
        user.setUsername(updatedUser.getUsername());
        return ResponseEntity.ok(repositoryUser.save(user));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = repositoryUser.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        repositoryUser.delete(user);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

}
