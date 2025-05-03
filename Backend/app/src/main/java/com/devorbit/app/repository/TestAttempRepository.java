package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devorbit.app.entity.Test;
import com.devorbit.app.entity.TestAttemp;
import com.devorbit.app.entity.User;

import java.util.List;


@Repository
public interface TestAttempRepository extends JpaRepository<TestAttemp, Integer> {
    
    List<TestAttemp> findByUser(User user);
    List<TestAttemp> findByTest(Test test);

    @Query(value = "SELECT count(*) FROM test_attempts WHERE user_id = :userId AND test_id = :testId", nativeQuery = true)
    int countByUserAndTest(@Param("userId") int userId, @Param("testId") int testId);
}
