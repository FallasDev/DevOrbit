package com.devorbit.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.devorbit.app.entity.User;
import com.devorbit.app.repository.RepositoryUser;
import com.devorbit.app.security.JwtUtil;
import com.devorbit.app.service.UserService;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*") 
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
       
        if (repositoryUser.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso");
        }

        if (repositoryUser.findByEmail(registrationRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword())); 
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setRole("ROLE_USER");

        repositoryUser.save(newUser);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthRequest authRequest) {
        try {
            User user = repositoryUser.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Credenciales incorrectas");
            }
            UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            String jwt = jwtUtil.generateToken(userDetails.getUsername(), role);

            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            return response;
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciales incorrectas");
        }
    }

    public static class AuthRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class UserRegistrationRequest {
        private String username;
        private String email;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}