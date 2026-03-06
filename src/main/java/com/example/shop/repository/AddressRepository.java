package com.example.shop.repository;

import com.example.shop.model.Address;
import com.example.shop.model.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);

    List<Address> findByUserIdAndType(Long userId, AddressType type);
}
