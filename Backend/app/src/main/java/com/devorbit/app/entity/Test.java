package com.devorbit.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tests")
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class Test {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int test_id;

    @Column(name = "instruction", nullable = false, length = 300)
    private String instruction;
    

    // Relacion con el curso cuando se añada
    @OneToOne(mappedBy = "test")
    private Course course;
    
}
