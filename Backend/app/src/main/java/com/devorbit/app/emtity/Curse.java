package com.devorbit.app.emtity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curses") // o "courses" si era un typo
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curse")
    private int id_curse;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    //llave foranea con dixon, esperar a reunion
    //@ManyToOne
    //@JoinColumn(name = "id_user")
    //private User user;

    @Column
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "picture_id")
    private Picture picture;
}