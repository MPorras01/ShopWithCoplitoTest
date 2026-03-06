package com.example.shop.service;

import com.example.shop.dto.RoleRequest;
import com.example.shop.exception.BadRequestException;
import com.example.shop.exception.ResourceNotFoundException;
import com.example.shop.model.Role;
import com.example.shop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
    }

    public Role create(RoleRequest request) {
        roleRepository.findByName(request.getName()).ifPresent(r -> {
            throw new BadRequestException("El rol ya existe: " + request.getName());
        });

        Role role = new Role();
        role.setName(request.getName());
        return roleRepository.save(role);
    }

    public Role update(Long id, RoleRequest request) {
        Role role = findById(id);
        role.setName(request.getName());
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        Role role = findById(id);
        roleRepository.delete(role);
    }
}
