package com.example.shop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CheckoutRequest {

    @NotNull
    @Valid
    private AddressRequest shippingAddress;

    @NotNull
    @Valid
    private AddressRequest billingAddress;

    @NotNull
    private Double shippingAmount;

    @NotNull
    private Double taxAmount;

    @NotNull
    private Double discountAmount;

    @NotBlank
    private String currency;

    public AddressRequest getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressRequest shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AddressRequest getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(AddressRequest billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Double getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(Double shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
