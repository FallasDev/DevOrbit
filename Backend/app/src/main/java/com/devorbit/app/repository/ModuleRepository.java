package com.devorbit.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devorbit.app.entity.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer> {

    @Query("SELECT m FROM Module m WHERE m.course.id_course = :courseId ORDER BY m.moduleOrder")
    List<Module> findByCourseId(@Param("courseId") int courseId);
}
