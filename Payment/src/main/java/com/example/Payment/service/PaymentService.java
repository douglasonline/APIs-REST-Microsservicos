package com.example.Payment.service;

import com.example.Payment.model.Payment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {

    Payment payment(Payment payment, String token, Long shoppingCartId);

    List<Payment> getAll();

}
