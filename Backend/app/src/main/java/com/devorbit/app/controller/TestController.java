package com.devorbit.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devorbit.app.entity.Answer;
import com.devorbit.app.entity.Test;
import com.devorbit.app.service.TestService;

@RestController
@CrossOrigin(origins = "*") // Permitir acceso desde cualquier origen
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Test> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Test getTestById(@PathVariable int id) {
        return testService.getTestById(id);
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTest(@PathVariable int id) {
        testService.deleteTest(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Test updateTest(@PathVariable int id, @RequestBody Test test) {
        return testService.updateTest(id, test);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Test saveTest(@RequestBody Test test) {
        return testService.saveTest(test);
    }

    @PostMapping("/{testId}/getScore")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public double getScore(@RequestBody List<Answer> userAnswers, @PathVariable int testId) {
        int countCorrectAnswers = testService.getCorrectAnswers(userAnswers);
        System.out.println(countCorrectAnswers);
        return testService.calculateScore(testId, countCorrectAnswers);
    }

    @GetMapping("/course/{courseId}")
    public Test getTestsByCourse(@PathVariable int courseId) {
        return testService.getTestByCourseId(courseId);
    }

}
