package com.devorbit.app.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.devorbit.app.entity.Payment;
import com.devorbit.app.repository.RepositoryPayment;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentService {

    @Autowired
    private RepositoryPayment repositoryPayment;

    public Payment add(Payment payment) {
        return repositoryPayment.save(payment);
    }

    public List<Payment> get() {
        return repositoryPayment.findAll();
    }

    public Optional<Payment> getById(int id) {
        return repositoryPayment.findById(id);
    }

    public void delete(int id) {
        repositoryPayment.deleteById(id);
    }

    public Payment update(int id, Payment payment) {
        Optional<Payment> existingPayment = repositoryPayment.findById(id);
        if (existingPayment.isPresent()) {
            Payment updatedPayment = existingPayment.get();
            updatedPayment.setInscription(payment.getInscription());
            updatedPayment.setMethodPayment(payment.getMethodPayment());
            updatedPayment.setCreateAt(payment.getCreateAt());
            updatedPayment.setTotal(payment.getTotal());
            updatedPayment.setUser(payment.getUser());
            return repositoryPayment.save(updatedPayment);
        } else {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
    }

}
