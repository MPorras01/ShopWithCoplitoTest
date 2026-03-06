package com.example.shop.service;

import com.example.shop.dto.AddressRequest;
import com.example.shop.model.Address;
import com.example.shop.model.AppUser;
import com.example.shop.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address createAddress(AppUser user, AddressRequest request) {
        Address address = new Address();
        address.setUser(user);
        address.setType(request.getType());
        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setLine1(request.getLine1());
        address.setLine2(request.getLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        return addressRepository.save(address);
    }
}
