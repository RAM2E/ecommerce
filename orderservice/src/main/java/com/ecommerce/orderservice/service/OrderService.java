package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(Long userId, OrderRequest orderRequest);
    List<OrderResponse> getUserOrders(Long userId);
    List<OrderResponse> getAllOrders();
    void updateOrderStatus(Long orderId, String status);
    String createStripePaymentSession(Long userId, OrderRequest orderRequest, String origin);
    void verifyStripePayment(Long orderId, boolean success);
}