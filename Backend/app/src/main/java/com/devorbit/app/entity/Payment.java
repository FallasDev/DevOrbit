package com.devorbit.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments") 
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private int idPayment;

    
    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_inscription", referencedColumnName = "id_inscription")
    private Inscription inscription;

    private LocalDateTime createAt;
    private BigDecimal total;
    private String methodPayment;


}