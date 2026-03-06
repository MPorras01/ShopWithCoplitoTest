package com.example.shop.repository;

import com.example.shop.model.Payment;
import com.example.shop.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByExternalReference(String externalReference);

    List<Payment> findByStatus(PaymentStatus status);
}
