package com.example.shop.service;

import com.example.shop.exception.BadRequestException;
import com.example.shop.exception.ResourceNotFoundException;
import com.example.shop.model.AppUser;
import com.example.shop.model.CartItem;
import com.example.shop.model.Product;
import com.example.shop.repository.AppUserRepository;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final AppUserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Autowired
    public CartService(CartItemRepository cartItemRepository,
                       AppUserRepository userRepository,
                       ProductRepository productRepository,
                       ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public List<CartItem> getUserCart(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public CartItem addItem(Long userId, Long productId, int quantity) {
        if (quantity < 1) {
            throw new BadRequestException("La cantidad debe ser al menos 1");
        }

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseGet(CartItem::new);

        item.setUser(user);
        item.setProduct(product);
        item.setQuantity((item.getQuantity() == null ? 0 : item.getQuantity()) + quantity);
        item.setUnitPrice(productService.getEffectiveSellingPrice(product));

        return cartItemRepository.save(item);
    }

    public CartItem updateItemQuantity(Long userId, Long productId, int quantity) {
        if (quantity < 1) {
            throw new BadRequestException("La cantidad debe ser al menos 1");
        }

        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado"));

        item.setQuantity(quantity);
        item.setUnitPrice(productService.getEffectiveSellingPrice(item.getProduct()));
        return cartItemRepository.save(item);
    }

    public void removeItem(Long userId, Long productId) {
        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado"));
        cartItemRepository.delete(item);
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    public double calculateSubtotal(Long userId) {
        return getUserCart(userId).stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }
}
