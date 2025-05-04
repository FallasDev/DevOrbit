package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devorbit.app.entity.Course;
import com.devorbit.app.entity.Test;
import java.util.List;


@Repository 
public interface TestRepository extends JpaRepository<Test, Integer> {
    
    List<Test> findByCourse(Course course);

}
