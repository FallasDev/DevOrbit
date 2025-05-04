package com.devorbit.app.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course")
    private int id_course;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column
    private boolean status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "picture_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Picture picture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Test test;





}