package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devorbit.app.entity.Test;

@Repository 
public interface TestRepository extends JpaRepository<Test, Integer> {
    
}
