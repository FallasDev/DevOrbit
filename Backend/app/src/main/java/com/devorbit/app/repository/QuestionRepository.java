package com.devorbit.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devorbit.app.entity.Question;
import com.devorbit.app.entity.Test;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByTest(Test test);
}
