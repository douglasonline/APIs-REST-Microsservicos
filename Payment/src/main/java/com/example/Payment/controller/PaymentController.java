package com.example.Payment.controller;

import com.example.Payment.model.Payment;
import com.example.Payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            // Use o token conforme necessário
            List<Payment> products = paymentService.getAll();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar os pagamento: " + e.getMessage());
        }
    }

    @Operation(summary = "Make a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/makePayment/{shoppingCartId}")
    public ResponseEntity<?> makePayment(@RequestBody Payment payment, @RequestHeader("Authorization") String token,
                                         @PathVariable Long shoppingCartId) {
        try {
            Payment processedPayment = paymentService.payment(payment, token, shoppingCartId);
            return ResponseEntity.ok(processedPayment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
