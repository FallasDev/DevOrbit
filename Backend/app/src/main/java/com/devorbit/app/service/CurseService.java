package com.devorbit.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.emtity.Curse;
import com.devorbit.app.repository.CurseRepository;

@Service
public class CurseService {

    @Autowired
    private CurseRepository curseRepository;

    public List<Curse> findAll() {//buscar la lista
        return curseRepository.findAll();
    }

    public Optional<Curse> findById(int id){//buscar por id
        return curseRepository.findById(id);
    }
    
    public Curse save(Curse curse){//agregar guardar
        return curseRepository.save(curse);
    }

    public void deleteById(int id) {//borrar por id
        curseRepository.deleteById(id);
    }

    public Curse update(int id, Curse curse){
        Optional<Curse> existCurse = curseRepository.findById(id);
        if (existCurse.isPresent()) {
            Curse updaCurse = existCurse.get();
            updaCurse.setDescription(curse.getDescription());
            return curseRepository.save(updaCurse);

        }else{
            throw new RuntimeException("Curso no encontrada con ID: " + id);
        }
    }


}
