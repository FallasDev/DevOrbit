package com.devorbit.app.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_module")
    private int id_module;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_course")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    @Column(name = "module_order", nullable = false)
    private int moduleOrder;

}
