package com.devorbit.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Answer;
import com.devorbit.app.entity.Question;
import com.devorbit.app.repository.AnswerRepository;
import com.devorbit.app.repository.QuestionRepository;

@Service
public class AnswerService {
    
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Answer saveAnswer(Answer answer) {
        try {
            Question question = questionRepository.findById(answer.getQuestion().getQuestion_id()).orElseThrow(() -> new RuntimeException("Question not found"));
            if(question.getType() == 1 && answer.getShortAnswer() != null) {
                throw new RuntimeException("A single-choice question cannot have a short answer.", null);
            }

            if (question.getType() == 2 && answer.isCorrect() == true && answer.getTitle() != null){
                throw new RuntimeException("A short-answer cannot be marked as correct.", null);
            }

            if (question.getType() == 1 && answer.getTitle() == null) {
                throw new RuntimeException("A single-choice question must have a title.", null);
            }

            if (question.getType() == 2 && answer.getShortAnswer() == null) {
                throw new RuntimeException("A short-answer question must have a short answer.", null);
            }
            
           
            answer.setQuestion(question);
            return answerRepository.save(answer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Answer getAnswerById(int id) {
        return answerRepository.findById(id).orElse(null);
    }

    public void deleteAnswer(int id) {
        answerRepository.deleteById(id);
    }

    public Answer updateAnswer(int id, Answer answer) {
        if (answerRepository.existsById(id)) {
            answer.setAnswer_id(id);
            return answerRepository.save(answer);
        }
        return null;
    }

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public List<Answer> getAnswersByQuestionId(int id){


        try {
            Question question = questionRepository.findById(id).orElseThrow(() -> new Exception("No existe la pregunta!"));
            return answerRepository.findByQuestion(question);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean checkIsCorrect(int questionId, Answer userAnswer) {
        Question question = questionRepository.findById(questionId).orElseThrow(null);

        if(question == null){
            return false;
        }

        List<Answer> answers = answerRepository.findByQuestion(question);

        for (Answer item : answers) {
            if(item.getAnswer_id() == userAnswer.getAnswer_id()){
                return item.isCorrect();
            }
        }
        return false;
    }
    
    public boolean checkIsCorrectShortAnswer(Answer userAnswer){

        Answer answer = answerRepository.findById(userAnswer.getAnswer_id()).orElse(null);

        if(answer == null){
            return false;
        }

        if(answer.getShortAnswer() == null){
            return false;
        }

        return answer.getShortAnswer().equals(userAnswer.getShortAnswer());

    }

    

}
