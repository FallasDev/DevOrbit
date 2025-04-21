package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devorbit.app.emtity.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer> {
}
