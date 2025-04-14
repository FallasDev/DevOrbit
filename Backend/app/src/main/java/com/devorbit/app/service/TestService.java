package com.devorbit.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Test;
import com.devorbit.app.repository.TestRepository;

@Service
public class TestService {
    
    @Autowired
    private TestRepository testRepository;
    
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

}
