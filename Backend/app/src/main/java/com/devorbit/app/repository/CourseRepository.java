package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devorbit.app.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
