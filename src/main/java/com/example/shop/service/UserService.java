package com.example.shop.service;

import com.example.shop.dto.AuthRegisterRequest;
import com.example.shop.dto.UserRequest;
import com.example.shop.dto.UserResponse;
import com.example.shop.exception.BadRequestException;
import com.example.shop.exception.ResourceNotFoundException;
import com.example.shop.model.AppUser;
import com.example.shop.model.Role;
import com.example.shop.model.RoleName;
import com.example.shop.repository.AppUserRepository;
import com.example.shop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(AppUserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public UserResponse create(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El usuario ya existe: " + request.getUsername());
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(request.isEnabled());
        user.setRoles(resolveRolesByIds(request.getRoleIds()));

        return toResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UserRequest request) {
        AppUser user = findEntityById(id);

        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El usuario ya existe: " + request.getUsername());
        }

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(request.isEnabled());
        user.setRoles(resolveRolesByIds(request.getRoleIds()));

        return toResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        AppUser user = findEntityById(id);
        userRepository.delete(user);
    }

    public AppUser register(AuthRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El usuario ya existe: " + request.getUsername());
        }

        Set<RoleName> roleNames = request.getRoles();
        if (roleNames == null || roleNames.isEmpty()) {
            roleNames = Collections.singleton(RoleName.ROLE_CUSTOMER);
        }

        Set<Role> roles = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + name)))
                .collect(Collectors.toSet());

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    }

    private AppUser findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    private Set<Role> resolveRolesByIds(Set<Long> roleIds) {
        return roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + roleId)))
                .collect(Collectors.toSet());
    }

    public UserResponse toResponse(AppUser user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEnabled(user.isEnabled());
        response.setRoles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
        return response;
    }
}
