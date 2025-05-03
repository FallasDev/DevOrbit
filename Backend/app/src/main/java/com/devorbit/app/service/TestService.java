package com.devorbit.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Answer;
import com.devorbit.app.entity.Course;
import com.devorbit.app.entity.Question;
import com.devorbit.app.entity.Test;
import com.devorbit.app.repository.QuestionRepository;
import com.devorbit.app.repository.TestRepository;

@Service
public class TestService {
    
    @Autowired
    private TestRepository testRepository;
    
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private CourseService courseService;

    public Test saveTest(Test test) {
        try {
            return testRepository.save(test);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Test getTestById(int id) {
        return testRepository.findById(id).orElse(null);
    }

    public void deleteTest(int id) {
        testRepository.deleteById(id);
    }

    public Test updateTest(int id, Test test) {
        if (testRepository.existsById(id)) {
            test.setTest_id(id);
            return testRepository.save(test);
        }
        return null;
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    public int getCorrectAnswers(List<Answer> userAnswers){

        int countCorrects = 0;

        for (Answer item : userAnswers) {
            
            Question nowQuestion = questionRepository.findById(item.getQuestion().getQuestion_id()).orElse(null);

            if (nowQuestion == null){
                continue;
            }

            item.setQuestion(nowQuestion);

            if(item.getQuestion().getType() == 1){
                System.out.println(item.getQuestion());
                if(answerService.checkIsCorrect(item.getQuestion().getQuestion_id(), item)) countCorrects++;
            }

            if(item.getQuestion().getType() == 2){
                if(answerService.checkIsCorrectShortAnswer(item)) countCorrects++;
            }
        }

        return countCorrects;
    }
    
    public double calculateScore(int testId, int countCorrectAnswers) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));
        List<Question> questions = questionRepository.findByTest(test);
        double question_value = 100 / questions.size();
        return question_value * countCorrectAnswers;
    }

    public Test getTestByCourseId(int courseId) {

        Course course = courseService.findById(courseId).get();

        if (course == null) {
            return null;
        }
        

        return testRepository.findByCourse(course).get(0);
    }


}
