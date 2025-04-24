package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devorbit.app.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    
}
