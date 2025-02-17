package com.ecommerce.orderservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private Long userId;
    private List<OrderItemDto> items;
    private Double amount;
    private AddressDto address;
    private String status;
    private String paymentMethod;
    private Boolean payment;
    private LocalDateTime createdAt;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long userId, List<OrderItemDto> items, Double amount, AddressDto address, String status, String paymentMethod, Boolean payment, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.amount = amount;
        this.address = address;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.payment = payment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Boolean getPayment() {
        return payment;
    }

    public void setPayment(Boolean payment) {
        this.payment = payment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id=" + id +
                ", userId=" + userId +
                ", items=" + items +
                ", amount=" + amount +
                ", address=" + address +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", payment=" + payment +
                ", createdAt=" + createdAt +
                '}';
    }
}
