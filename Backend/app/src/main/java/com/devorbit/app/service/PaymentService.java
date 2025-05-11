package com.devorbit.app.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.devorbit.app.entity.Payment;
import com.devorbit.app.entity.User;
import com.devorbit.app.repository.RepositoryPayment;
import com.devorbit.app.repository.RepositoryUser;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentService {

    @Autowired
    private RepositoryPayment repositoryPayment;

    @Autowired
    private RepositoryUser userRepository;

    public Payment add(Payment payment) {
        User user = userRepository.findById(payment.getUser().getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        payment.setUser(user);
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

    public boolean existsByPaypalPaymentId(String paypalPaymentId) {
        return repositoryPayment.existsByPaypalPaymentId(paypalPaymentId);
    }

    public Payment update(int id, Payment payment) {
        Optional<Payment> existingPayment = repositoryPayment.findById(id);
        if (existingPayment.isPresent()) {
            Payment updatedPayment = existingPayment.get();

            updatedPayment.setInscription(payment.getInscription());
            updatedPayment.setMethodPayment(payment.getMethodPayment());
            updatedPayment.setCreateAt(payment.getCreateAt());
            updatedPayment.setTotal(payment.getTotal());

            User user = userRepository.findById(payment.getUser().getIdUser())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            updatedPayment.setUser(user);

            return repositoryPayment.save(updatedPayment);
        } else {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
    }
}
