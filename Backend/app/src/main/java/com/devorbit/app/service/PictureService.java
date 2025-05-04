package com.devorbit.app.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Picture;
import com.devorbit.app.entity.Course;
import com.devorbit.app.repository.CourseRepository;
import com.devorbit.app.repository.PictureRepository;

@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private CourseRepository courseRepository;


    public List<Picture> findAll() {
        return pictureRepository.findAll();
    }

    public Optional<Picture> findById(int id) {
        return pictureRepository.findById(id);
    }


    public Picture save(Picture picture) {
        return pictureRepository.save(picture);
    }


    public void deleteById(int id) {
        pictureRepository.deleteById(id);
    }


    public Picture update(int id, Picture picture) {
        if (pictureRepository.existsById(id)) {
            picture.setId_picture(id);
            return pictureRepository.save(picture);
        }
        return null;
    }

    public Picture savePicture(Map<String, Object> uploadResult, int id_course) {

        Course course = courseRepository.findById(id_course).orElse(null);
        if (course == null) {
            return null;  
        }
        Picture picture = new Picture();
        picture.setUrl(uploadResult.get("secure_url").toString()); 
    
        picture.setCourse(course);
    
        try {
            return pictureRepository.save(picture); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
