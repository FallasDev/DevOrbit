package com.devorbit.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "questions")
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int question_id;

    @Column(name = "prompt", nullable = false, length = 124)
    private String prompt;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Override
    public String toString() {
        return "Question{" +
                "question_id=" + question_id +
                ", prompt='" + prompt + '\'' +
                ", test=" + test +
                '}';
    }

}
