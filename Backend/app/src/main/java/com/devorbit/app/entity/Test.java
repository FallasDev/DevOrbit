package com.devorbit.app.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    
    @Column(name = "max_attemps", nullable = false)
    private int maxAttemps;

    // Relacion con el curso cuando se añada
    @OneToOne(mappedBy = "test")
    @JsonIgnore
    private Course course;

    @OneToMany(mappedBy = "test")
    @JsonIgnore
    private Set<TestAttemp> testAttemps;

    @Override
    public String toString() {
        return "Test{" +
                "test_id=" + test_id +
                ", instruction='" + instruction + '\'' +
                ", maxAttemps=" + maxAttemps +
                '}';
    }   
}
