package com.devorbit.app.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.devorbit.app.entity.User;
import com.devorbit.app.repository.RepositoryUser;
import org.springframework.security.core.Authentication;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private RepositoryUser repositoryUser;

    public User add(User user) {
        return repositoryUser.save(user);
    }

    public List<User> get() {
        return repositoryUser.findAll();
    }

    public Optional<User> getById(int id) {
        return repositoryUser.findById(id);
    }

    public void delete(int id) {
        repositoryUser.deleteById(id);
    }

    public User update(int id, User user) {
        Optional<User> existingUser = repositoryUser.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setCreatedAt(user.getCreatedAt());
            updatedUser.setRole(user.getRole());

            return repositoryUser.save(updatedUser);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repositoryUser.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority(user.getRole())) 
        );
    }

    public User findByEmail(String email) {
        return repositoryUser.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }

    public User updateUser(String email, User updatedData) {
        User user = findByEmail(email);
    
        user.setUsername(updatedData.getUsername());
    

        if (updatedData.getPassword() != null && !updatedData.getPassword().isBlank()) {
            user.setPassword(updatedData.getPassword()); 
        }
    
        return repositoryUser.save(user);
    }

    public void deleteByEmail(String email) {
        User user = findByEmail(email);
        repositoryUser.delete(user);
    }
    
    public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    return repositoryUser.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
}

}
