package com.example.shop.service;

import com.example.shop.dto.AuthLoginRequest;
import com.example.shop.dto.AuthResponse;
import com.example.shop.dto.AuthRegisterRequest;
import com.example.shop.model.AppUser;
import com.example.shop.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UserService userService,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AppUser appUser = userService.findByUsername(userDetails.getUsername());

        String token = jwtService.generateToken(userDetails);
        Set<String> roles = appUser.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());

        return new AuthResponse(token, appUser.getUsername(), roles);
    }

    public AuthResponse register(AuthRegisterRequest request) {
        AppUser appUser = userService.register(request);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities(appUser.getRoles().stream().map(role -> role.getName().name()).toArray(String[]::new))
                .build();

        String token = jwtService.generateToken(userDetails);
        Set<String> roles = appUser.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
        return new AuthResponse(token, appUser.getUsername(), roles);
    }
}
