package com.devorbit.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.emtity.Module;
import com.devorbit.app.repository.ModuleRepository;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    public List<Module> findAll() {//buscar la lista
        return moduleRepository.findAll();
    }

    public Optional<Module> findById(int id){//buscar por id
        return moduleRepository.findById(id);
    }
    
    public Module save(Module module){//agregar guardar
        return moduleRepository.save(module);
    }

    public void deleteById(int id) {//borrar por id
        moduleRepository.deleteById(id);
    }

    public Module update(int id, Module module){
        Optional<Module> existModule = moduleRepository.findById(id);
        if (existModule.isPresent()) {
            Module updatedModule = existModule.get();
            updatedModule.setDescription(module.getDescription());
            return moduleRepository.save(updatedModule);

        }else{
            throw new RuntimeException("Curso no encontrada con ID: " + id);
        }
    }

}
