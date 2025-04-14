package com.devorbit.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devorbit.app.entity.Test;
import com.devorbit.app.service.TestService;

@RestController
@RequestMapping("/api/tests")
public class TestController {
    
    @Autowired
    private TestService testService;
    
    @GetMapping
    public List<Test> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/{id}")
    public Test getTestById(@PathVariable int id) {
        return testService.getTestById(id);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteTest(@PathVariable int id) {
        testService.deleteTest(id);
    }
    @PutMapping("/{id}")
    public Test updateTest(@PathVariable int id, @RequestBody Test test) {
        return testService.updateTest(id, test);
    }

    @PostMapping
    public Test saveTest(@RequestBody Test test) {
        return testService.saveTest(test);
    }

}
