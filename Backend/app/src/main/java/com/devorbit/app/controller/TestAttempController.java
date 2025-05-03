package com.devorbit.app.controller;

import java.util.List;
import java.util.Optional;

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

import com.devorbit.app.entity.TestAttemp;
import com.devorbit.app.service.TestsAttempService;

@RestController
@CrossOrigin(origins = "*") // Permitir acceso desde cualquier origen
@RequestMapping("/api/test-attempts")
public class TestAttempController {
    
    @Autowired
    private TestsAttempService testsAttempService;
    
    
    @GetMapping
    public List<TestAttemp> getAllTestAttemp(){
        return testsAttempService.getAllTestAttemps();
    }


    @GetMapping("/{id}")
    public TestAttemp getTestAttempById(@PathVariable int id) {
        return testsAttempService.getTestAttempById(id);
    }

    @PostMapping
    public ResponseEntity<?> createTestAttemp(@RequestBody TestAttemp testAttemp) {
        try {
            return ResponseEntity.ok(testsAttempService.saveTestAttemp(testAttemp));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public TestAttemp updateTestAttemp(@PathVariable int id, @RequestBody TestAttemp testAttemp) {
        return testsAttempService.updateTestAttemp(id, testAttemp);
    }
    

    @DeleteMapping("/{id}")
    public void deleteTestAttemp(@PathVariable int id) {
        testsAttempService.deleteTestAttemp(id);
    }

    @GetMapping("/user/{userId}")
    public List<TestAttemp> getTestAttempsByUserId(@PathVariable int userId) {
        return testsAttempService.getTestAttempsByUserId(userId);
    }

    @GetMapping("/test/{testId}")
    public List<TestAttemp> getTestAttempsByTestId(@PathVariable int testId) {
        return testsAttempService.getTestAttempsByTestId(testId);
    }


}
