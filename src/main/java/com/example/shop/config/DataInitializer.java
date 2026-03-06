package com.example.shop.config;

import com.example.shop.model.AppUser;
import com.example.shop.model.Role;
import com.example.shop.model.RoleName;
import com.example.shop.repository.AppUserRepository;
import com.example.shop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin.username:admin}")
    private String adminUsername;

    @Value("${app.bootstrap.admin.password:admin123}")
    private String adminPassword;

    @Autowired
    public DataInitializer(RoleRepository roleRepository,
                           AppUserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        for (RoleName roleName : EnumSet.allOf(RoleName.class)) {
            roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
        }

        if (!userRepository.existsByUsername(adminUsername)) {
            Set<Role> adminRoles = Set.of(
                    roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(),
                    roleRepository.findByName(RoleName.ROLE_MANAGER).orElseThrow()
            ).stream().collect(Collectors.toSet());

            AppUser admin = new AppUser();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEnabled(true);
            admin.setRoles(adminRoles);
            userRepository.save(admin);
        }
    }
}
