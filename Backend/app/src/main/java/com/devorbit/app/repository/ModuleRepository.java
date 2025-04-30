package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devorbit.app.entity.Course;
import com.devorbit.app.entity.Module;
import java.util.List;


public interface ModuleRepository extends JpaRepository<Module, Integer> {
    @Query(value = "SELECT * FROM modules WHERE id_course = :id ORDER BY module_order ASC", nativeQuery = true)
    List<Module> findByCourse(@Param("id") int idCourse);
}
