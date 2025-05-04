package com.devorbit.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devorbit.app.entity.Course;
import com.devorbit.app.entity.Module;
import com.devorbit.app.repository.ModuleRepository;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseService courseService;

    public List<Module> findAll() {// buscar la lista
        return moduleRepository.findAll();
    }

    public Optional<Module> findById(int id) {// buscar por id
        return moduleRepository.findById(id);
    }

    public Module save(String title, String descripcion, int courseId, List<Integer> moduleOrder) {// agregar guardar

        Course course = courseService.findById(courseId).orElse(null);

        Module module = new Module();

        module.setTitle(title);
        module.setDescription(descripcion);

        if (course == null) {
            return null;
        }

        module.setCourse(course);

        int ID_MODULE_NEW = 0;

        for (int i = 0; i < moduleOrder.size(); i++) {

            int new_order = i + 1;

            if (moduleOrder.get(i) == ID_MODULE_NEW) {
                module.setModuleOrder(new_order);
            }

            Module nowModule = moduleRepository.findById(moduleOrder.get(i)).orElse(null);
            if (nowModule == null) {
                continue;
            }

            nowModule.setModuleOrder(new_order);
        }

        try {
            return moduleRepository.save(module);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void deleteById(int id) {// borrar por id
        moduleRepository.deleteById(id);
    }

    public List<Module> findByCourseId(int courseId) {
        return moduleRepository.findByCourse(courseId);
    }

    public Module update(int id, String title, String descripcion, int courseId, List<Integer> moduleOrder) {
        Course course = courseService.findById(courseId).orElse(null);

        Module module = moduleRepository.findById(id).orElse(null);

        if (module == null) {
            return null;
        }

        module.setTitle(title);
        module.setDescription(descripcion);

        if (course == null) {
            return null;
        }

        module.setCourse(course);

        int ID_MODULE_NEW = 0;

        for (int i = 0; i < moduleOrder.size(); i++) {

            int new_order = i + 1;

            if (moduleOrder.get(i) == ID_MODULE_NEW) {
                module.setModuleOrder(new_order);
            }

            Module nowModule = moduleRepository.findById(moduleOrder.get(i)).orElse(null);
            if (nowModule == null) {
                continue;
            }

            nowModule.setModuleOrder(new_order);
        }

        try {
            return moduleRepository.save(module);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
