package com.devorbit.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter @Getter
@Table(name = "test_attempts")
public class TestAttemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attemptId;

    @Column(name = "score", nullable = false)
    private int score;
    
    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    public String toString() {
        return "TestAttemp{" +
                "attemptId=" + attemptId +
                ", score=" + score +
                ", test=" + test.getInstruction() +
                ", user=" + user.getUsername() +
                '}';
    }

}
