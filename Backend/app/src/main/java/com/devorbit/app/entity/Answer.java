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
@Table(name = "answers")
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class Answer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int answer_id;

    @Column(name = "title", nullable = false, length = 124)
    private String title;

    @Column(name = "is_correct")
    private boolean correct;

    @Column(name = "short_answer", length = 54)
    private String shortAnswer;


    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Override
    public String toString() {
        return "Answer{" +
                "answer_id=" + answer_id +
                ", title='" + title + '\'' +
                ", isCorrect=" + correct +
                ", shortAnswer='" + shortAnswer + '\'' +
                ", question=" + question +
                '}';
    }
    
    
}
