package com.devorbit.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pictures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_picture")
    private int id_picture;

    @Column(name = "url", nullable = false, length = 200)
    private String url;

    // Añadir relacion con modulos cuando esten
 
    @OneToOne(mappedBy = "picture")
    @JsonIgnore
    private Course course;

}
