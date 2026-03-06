package com.example.shop.service;

import com.example.shop.dto.ProductRequest;
import com.example.shop.exception.BadRequestException;
import com.example.shop.exception.ResourceNotFoundException;
import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;

    @Autowired
    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Product findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    public Product create(ProductRequest request) {
        Product product = new Product();
        mapRequestToProduct(request, product);
        return repo.save(product);
    }

    public Product update(Long id, ProductRequest request) {
        Product product = findById(id);
        mapRequestToProduct(request, product);
        return repo.save(product);
    }

    public void delete(Long id) {
        Product product = findById(id);
        repo.delete(product);
    }

    private void mapRequestToProduct(ProductRequest request, Product product) {
        validatePricing(request);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCost(request.getCost() == null ? 0.0 : request.getCost());
        product.setPrice(request.getPrice());
        product.setSalePrice(request.getSalePrice());
        product.setStock(request.getStock());
        product.setImageUrls(request.getImageUrls() == null ? new ArrayList<>() : new ArrayList<>(request.getImageUrls()));
    }

    private void validatePricing(ProductRequest request) {
        if (request.getCost() != null && request.getCost() > request.getPrice()) {
            throw new BadRequestException("El costo no puede ser mayor al precio base de venta");
        }

        if (request.getSalePrice() != null && request.getSalePrice() > request.getPrice()) {
            throw new BadRequestException("El precio promocional no puede ser mayor al precio base");
        }
    }

    public double getEffectiveSellingPrice(Product product) {
        if (product.getSalePrice() != null && product.getSalePrice() > 0.0) {
            return product.getSalePrice();
        }
        return product.getPrice();
    }

    public double calculateMarginAmount(Product product) {
        return getEffectiveSellingPrice(product) - product.getCost();
    }

    public double calculateMarginPercent(Product product) {
        double effectivePrice = getEffectiveSellingPrice(product);
        if (effectivePrice == 0.0) {
            return 0.0;
        }
        return (calculateMarginAmount(product) / effectivePrice) * 100.0;
    }
}