package com.example.shop.service;

import com.example.shop.dto.CheckoutRequest;
import com.example.shop.exception.ResourceNotFoundException;
import com.example.shop.model.*;
import com.example.shop.repository.AppUserRepository;
import com.example.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AppUserRepository userRepository;
    private final CartService cartService;
    private final AddressService addressService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        AppUserRepository userRepository,
                        CartService cartService,
                        AddressService addressService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.addressService = addressService;
    }

    public Order createOrderFromCart(Long userId, CheckoutRequest request) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<CartItem> cartItems = cartService.getUserCart(userId);
        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("El carrito no tiene items");
        }

        Address shippingAddress = addressService.createAddress(user, request.getShippingAddress());
        Address billingAddress = addressService.createAddress(user, request.getBillingAddress());

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCurrency(request.getCurrency());
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);

        double subtotal = 0.0;
        for (CartItem cartItem : cartItems) {
            OrderItem item = new OrderItem();
            item.setProduct(cartItem.getProduct());
            item.setProductName(cartItem.getProduct().getName());
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getUnitPrice());
            item.setLineTotal(cartItem.getUnitPrice() * cartItem.getQuantity());
            subtotal += item.getLineTotal();
            order.addItem(item);
        }

        order.setSubtotal(subtotal);
        order.setDiscountAmount(request.getDiscountAmount());
        order.setShippingAmount(request.getShippingAmount());
        order.setTaxAmount(request.getTaxAmount());
        order.setTotalAmount(subtotal - request.getDiscountAmount() + request.getShippingAmount() + request.getTaxAmount());

        Order saved = orderRepository.save(order);
        cartService.clearCart(userId);
        return saved;
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
