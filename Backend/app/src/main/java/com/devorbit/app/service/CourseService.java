package com.devorbit.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Course;
import com.devorbit.app.repository.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository curseRepository;

    public List<Course> findAll() {//buscar la lista
        return curseRepository.findAll();
    }

    public Optional<Course> findById(int id){//buscar por id
        return curseRepository.findById(id);
    }
    
    public Course save(Course curse){//agregar guardar
        return curseRepository.save(curse);
    }

    public void deleteById(int id) {//borrar por id
        curseRepository.deleteById(id);
    }

  public Course update(int id, Course curse){
        Optional<Course> existCurse = curseRepository.findById(id);
        if (existCurse.isPresent()) {
            Course updaCurse = existCurse.get();
            updaCurse.setDescription(curse.getDescription());
            updaCurse.setTitle(curse.getTitle());
            updaCurse.setPrice(curse.getPrice());
            return curseRepository.save(updaCurse);
        }else{
            throw new RuntimeException("Curso no encontrada con ID: " + id);
        }
    }  


}
