package com.example.shop.service;

import com.example.shop.exception.ResourceNotFoundException;
import com.example.shop.model.Order;
import com.example.shop.model.OrderStatus;
import com.example.shop.model.Payment;
import com.example.shop.model.PaymentStatus;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public Payment registerPaymentAttempt(Long orderId, String provider, String currency, String externalReference) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setProvider(provider);
        payment.setCurrency(currency);
        payment.setAmount(order.getTotalAmount());
        payment.setExternalReference(externalReference);
        payment.setStatus(PaymentStatus.INITIATED);

        return paymentRepository.save(payment);
    }

    public Payment confirmPayment(Long paymentId, boolean approved, String providerResponse) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

        payment.setProviderResponse(providerResponse);
        payment.setConfirmedAt(Instant.now());

        Order order = payment.getOrder();
        if (approved) {
            payment.setStatus(PaymentStatus.APPROVED);
            order.setStatus(OrderStatus.PAID);
        } else {
            payment.setStatus(PaymentStatus.DECLINED);
        }

        orderRepository.save(order);
        return paymentRepository.save(payment);
    }
}
