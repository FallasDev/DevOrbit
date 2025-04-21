package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devorbit.app.emtity.Curse;

public interface CurseRepository extends JpaRepository<Curse, Integer> {
}
