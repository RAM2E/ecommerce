package com.ecommerce.paymentservice.service;



import java.util.List;

import com.ecommerce.paymentservice.model.Payment;

public interface PaymentService {
    Payment createPayment(Payment payment);

    Payment getPaymentById(Long id);

    List<Payment> getAllPayments();

    Payment updatePayment(Long id, Payment payment);

    void deletePayment(Long id);
}
