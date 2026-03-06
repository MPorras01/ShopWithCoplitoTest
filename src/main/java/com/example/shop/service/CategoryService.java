package com.example.shop.service;

import com.example.shop.dto.CategoryRequest;
import com.example.shop.exception.BadRequestException;
import com.example.shop.exception.ResourceNotFoundException;
import com.example.shop.model.Category;
import com.example.shop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
    }

    public Category create(CategoryRequest request) {
        categoryRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing -> {
            throw new BadRequestException("Ya existe una categoria con ese nombre");
        });

        Category category = new Category();
        mapRequest(request, category);
        return categoryRepository.save(category);
    }

    public Category update(Long id, CategoryRequest request) {
        Category category = findById(id);
        categoryRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("Ya existe una categoria con ese nombre");
            }
        });

        mapRequest(request, category);
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }

    private void mapRequest(CategoryRequest request, Category category) {
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());
    }
}
