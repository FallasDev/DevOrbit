package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devorbit.app.entity.Picture;

public interface PictureRepository extends JpaRepository<Picture, Integer> {
}
