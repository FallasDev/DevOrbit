package com.devorbit.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devorbit.app.entity.Answer;
import com.devorbit.app.service.AnswerService;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    
    @Autowired
    private AnswerService answerService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Answer> getAllAnswers() {
        return answerService.getAllAnswers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Answer getAnswerById(@PathVariable int id) {
        return answerService.getAnswerById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> saveAnswer(@RequestBody Answer answer) {
        try{
            return ResponseEntity.ok(answerService.saveAnswer(answer));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving answer: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')") 
    public void deleteAnswer(@PathVariable int id) {
        answerService.deleteAnswer(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") 
    public Answer updateAnswer(@PathVariable int id, @RequestBody Answer answer) {
        return answerService.updateAnswer(id, answer);
    }
    



}
