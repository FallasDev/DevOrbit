package com.devorbit.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Question;
import com.devorbit.app.entity.Test;
import com.devorbit.app.repository.QuestionRepository;
import com.devorbit.app.repository.TestRepository;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository; 

    public Question saveQuestion(Question question) {
        try {

            Test test = testRepository.findById(question.getTest().getTest_id()).orElseThrow(() -> new RuntimeException("Test not found"));
            question.setTest(test);
            return questionRepository.save(question);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Question getQuestionById(int id) {
        return questionRepository.findById(id).orElse(null);
    }

    public void deleteQuestion(int id) {
        questionRepository.deleteById(id);
    }

    public Question updateQuestion(int id, Question question) {
        if (questionRepository.existsById(id)) {
            question.setQuestion_id(id);
            return questionRepository.save(question);
        }
        return null;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByTestId(int id){

        Test test;
        try {
            test = testRepository.findById(id).orElseThrow(() -> new Exception("El test no existe"));
            return questionRepository.findByTest(test);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    

    }

}
