package com.pelicanwork.pelicanwork_backend.repository;

import com.pelicanwork.pelicanwork_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // Find payments by order ID
    List<Payment> findByOrderId(Long orderId);
    
    // Find payment by payment gateway ID
    Payment findByPaymentGatewayId(String paymentGatewayId);
}
