package com.devorbit.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Course;
import com.devorbit.app.repository.RepositoryCourse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseService {

    @Autowired
    private RepositoryCourse repositoryCourse;

    public Course add(Course course) {
        return repositoryCourse.save(course);
    }

    public List<Course> get() {
        return repositoryCourse.findAll();
    }

    public Optional<Course> getById(int id) {
        return repositoryCourse.findById(id);
    }

    public void delete(int id) {
        repositoryCourse.deleteById(id);
    }

    public Course update(int id, Course course) {
        Optional<Course> existingCourse = repositoryCourse.findById(id);
        if (existingCourse.isPresent()) {
            Course updatedCourse = existingCourse.get();
            updatedCourse.setTitle(course.getTitle());
            updatedCourse.setDescription(course.getDescription());
            updatedCourse.setPrice(course.getPrice());
            updatedCourse.setStatus(course.isStatus());
            return repositoryCourse.save(updatedCourse);
        } else {
            throw new RuntimeException("Curso no encontrado con ID: " + id);
        }
    }
}
