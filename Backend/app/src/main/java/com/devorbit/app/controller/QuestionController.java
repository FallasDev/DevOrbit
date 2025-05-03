package com.devorbit.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devorbit.app.entity.Question;
import com.devorbit.app.service.QuestionService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/questions")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable int id) {
        return questionService.getQuestionById(id);
    }

    @GetMapping("/tests/{id}")
    public List<Question> getQuestionsByTestId(@PathVariable int id){
        return questionService.getQuestionsByTestId(id);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteQuestion(@PathVariable int id) {
        questionService.deleteQuestion(id);
    }

    @PostMapping
    public ResponseEntity<?> saveQuestion(@RequestBody Question question) {
        try {
            System.out.println("Saving question: " + question);
            return ResponseEntity.ok(questionService.saveQuestion(question));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error saving question: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable int id, @RequestBody Question question) {
        return questionService.updateQuestion(id, question);
    }


}

