package com.devorbit.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import io.swagger.v3.oas.annotations.tags.Tag;

import com.devorbit.app.entity.Picture;
import com.devorbit.app.service.PictureService;

@CrossOrigin(origins = "*") // Permitir acceso desde cualquier origen
//@Tag(name = "Pictures", description = "API para gestionar fotos") // Grupo en Swagger

@RestController
@RequestMapping("/api/pictures")
public class PictureController {
    
    @Autowired
    private PictureService pictureService;

    @GetMapping
    public List<Picture> getAllPictures(){
        return pictureService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Picture> getPictureById(@PathVariable int id) {
        return pictureService.findById(id);
    }

    @PostMapping
    public Picture createPicture(@RequestBody Picture picture) {
        return pictureService.save(picture);
    }

    @DeleteMapping("/{id}")
    public void deletePicture(@PathVariable int id) {
        pictureService.deleteById(id);
    }

}
