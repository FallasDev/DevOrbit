package com.devorbit.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.devorbit.app.entity.Payment;
import com.devorbit.app.entity.User;

public interface RepositoryPayment extends JpaRepository<Payment, Integer> {

    List<Payment> findByUser(User user);
    boolean existsByPaypalPaymentId(String paypalPaymentId);
    
}
