package com.devorbit.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devorbit.app.entity.Video;
import java.util.List;


@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    

    @Query(value = "SELECT * FROM videos WHERE module_id = :id ORDER BY video_order ASC", nativeQuery = true)
    List<Video> findByIdModule(@Param("id") int idModule);
}
