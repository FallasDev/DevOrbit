package com.devorbit.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Test;
import com.devorbit.app.entity.TestAttemp;
import com.devorbit.app.entity.User;
import com.devorbit.app.repository.TestAttempRepository;

@Service
public class TestsAttempService {

    @Autowired
    private TestAttempRepository testAttempRepository;

    @Autowired
    private TestService testService;

    @Autowired
    private UserService userService;

    public TestAttemp saveTestAttemp(TestAttemp testAttemp) {

        Test test = testService.getTestById(testAttemp.getTest().getTest_id());

        System.out.println(testAttemp.getTest());

        if (test == null) {
            return null; // or throw an exception
        }

        User user = userService.getById(testAttemp.getUser().getIdUser()).get();

        System.out.println(testAttemp.getUser().getUsername());

        if (user == null) {
            return null; // or throw an exception
        }

        testAttemp.setTest(test);
        testAttemp.setUser(user);

        System.out.println(testAttemp);

        if (test.getMaxAttemps() >= testAttempRepository.countByUserAndTest(user.getIdUser(), test.getTest_id())) {
            throw new RuntimeException("Max attemps reached");
        }
        
        return testAttempRepository.save(testAttemp);
    }

    public List<TestAttemp> getAllTestAttemps() {
        return testAttempRepository.findAll();
    }

    public TestAttemp getTestAttempById(int id) {
        return testAttempRepository.findById(id).orElse(null);
    }

    public void deleteTestAttemp(int id) {
        testAttempRepository.deleteById(id);
    }

    public TestAttemp updateTestAttemp(int id, TestAttemp testAttemp) {
        TestAttemp existingTestAttemp = getTestAttempById(id);
        if (existingTestAttemp != null) {
            existingTestAttemp.setScore(testAttemp.getScore());
            existingTestAttemp.setTest(testAttemp.getTest());
            existingTestAttemp.setUser(testAttemp.getUser());
            return testAttempRepository.save(existingTestAttemp);
        }
        return null;
    }

    public List<TestAttemp> getTestAttempsByUserId(int userId) {

        User user = userService.getById(userId).get();

        if (user == null) {
            return null; // or throw an exception
        }

        return testAttempRepository.findByUser(user);
    }

    public List<TestAttemp> getTestAttempsByTestId(int testId) {

        Test test = testService.getTestById(testId);

        if (test == null) {
            return null; // or throw an exception
        }

        return testAttempRepository.findByTest(test);
    }
    
}