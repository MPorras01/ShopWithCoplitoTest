package com.example.shop.dto;

import com.example.shop.model.RoleName;
import jakarta.validation.constraints.NotNull;

public class RoleRequest {

    @NotNull
    private RoleName name;

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}
