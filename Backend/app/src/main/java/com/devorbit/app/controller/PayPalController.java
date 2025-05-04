package com.devorbit.app.controller;

import com.devorbit.app.entity.Course;
import com.devorbit.app.entity.Inscription;
import com.devorbit.app.entity.User;
import com.devorbit.app.service.CourseService;
import com.devorbit.app.service.InscriptionService;
import com.devorbit.app.service.PaymentService;
import com.devorbit.app.repository.RepositoryUser;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/payments")
public class PayPalController {

    @Autowired
    private APIContext apiContext;

    @Autowired
    private CourseService courseService;

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private InscriptionService inscriptionService;

    @PostMapping("/create")
    public Map<String, String> createPayment(@RequestParam int courseId, @RequestParam String currency) {
        Map<String, String> response = new HashMap<>();

        try {
            // Obtener el curso desde el servicio
            Course course = courseService.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            BigDecimal total = course.getPrice();

            // Configurar el monto del pago
            Amount amount = new Amount();
            amount.setCurrency(currency);
            amount.setTotal(String.format("%.2f", total));

            // Crear la transacción
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("Pago por curso: " + course.getTitle());

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            // Configurar el pagador
            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            // Crear el objeto de pago
            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);

            // Configurar las URLs de redirección
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("http://localhost:5500/Frontend/error.html");
            redirectUrls.setReturnUrl("http://localhost:5500/Frontend/success.html?courseId=" + courseId);
            payment.setRedirectUrls(redirectUrls);

            // Crear el pago en PayPal
            Payment createdPayment = payment.create(apiContext);
            response.put("status", "success");
            response.put("redirect_url", createdPayment.getLinks().stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No approval_url found"))
                    .getHref());
        } catch (PayPalRESTException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    @PostMapping("/execute")
    public Map<String, String> executePayment(@RequestParam String paymentId, @RequestParam String payerId,
            @RequestParam int courseId, @RequestParam int userId) {
        Map<String, String> response = new HashMap<>();
        System.out.println("Course ID recibido: " + courseId);
        try {

            Payment payment = new Payment();
            payment.setId(paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            Course course = courseService.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

            User user = repositoryUser.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Inscription inscription = new Inscription();
            inscription.setUser(user);
            inscription.setCourse(course);
            inscription.setCreateAt(LocalDateTime.now());
            inscription.setProgress(0);
            Inscription createdInscription = inscriptionService.add(inscription);

            com.devorbit.app.entity.Payment paymentEntity = new com.devorbit.app.entity.Payment();
            paymentEntity.setUser(user);
            paymentEntity.setCreateAt(LocalDateTime.now());
            paymentEntity.setTotal(course.getPrice());
            paymentEntity.setMethodPayment("PayPal");
            paymentEntity.setInscription(createdInscription);
            paymentService.add(paymentEntity);

            // Responder con éxito
            response.put("status", "success");
            response.put("redirect_url", "/Frontend/course.html?courseId=" + courseId);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("redirect_url", "/Frontend/error.html");
        }
        return response;
    }
}