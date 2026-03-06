package com.example.shop.dto;

import com.example.shop.model.RoleName;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class AuthRegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Set<RoleName> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleName> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleName> roles) {
        this.roles = roles;
    }
}
