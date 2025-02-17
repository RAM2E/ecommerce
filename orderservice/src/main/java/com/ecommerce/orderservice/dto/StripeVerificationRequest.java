package com.ecommerce.orderservice.dto;

public class StripeVerificationRequest {
    private Long orderId;
    private boolean success;

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}