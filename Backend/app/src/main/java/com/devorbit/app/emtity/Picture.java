package com.devorbit.app.emtity;

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
    private int id;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

}
