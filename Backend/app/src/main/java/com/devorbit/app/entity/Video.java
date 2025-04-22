package com.devorbit.app.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "videos")
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class Video{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int video_id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "url", nullable = false, length = 200)
    private String url;

    @Column(name = "duration_seg", nullable = false)
    private int duration_seg;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    //Añadir relacion con modulos cuando esten
    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    
}