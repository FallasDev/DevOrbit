package com.devorbit.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.emtity.Picture;
import com.devorbit.app.repository.PictureRepository;

@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;

    public List<Picture> findAll() {// buscar la lista
        return pictureRepository.findAll();
    }

    public Optional<Picture> findById(int id) {// buscar por id
        return pictureRepository.findById(id);
    }

    public Picture save(Picture picture) {// agregar guardar
        return pictureRepository.save(picture);
    }

    public void deleteById(int id) {// borrar por id
        pictureRepository.deleteById(id);
    }

    public Picture update(int id, Picture picture) {
        Optional<Picture> existPicture = pictureRepository.findById(id);
        if (existPicture.isPresent()) {
            Picture updatedPicture = existPicture.get();
            updatedPicture.setUrl(picture.getUrl()); // Solo se actualiza la URL
            return pictureRepository.save(updatedPicture);
        } else {
            throw new RuntimeException("Picture no encontrada con ID: " + id);
        }
    }
}
