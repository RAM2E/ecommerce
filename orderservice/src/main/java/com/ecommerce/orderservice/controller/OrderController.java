package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.dto.StripeVerificationRequest;
import com.ecommerce.orderservice.dto.UpdateStatusRequest;
import com.ecommerce.orderservice.service.OrderService;
import com.ecommerce.orderservice.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> placeOrder(@RequestBody OrderRequest request) {
        Long userId = getCurrentUserId();
        OrderResponse response = orderService.placeOrder(userId, request);
        return ResponseEntity.ok(
            ApiResponse.builder()
                .success(true)
                .message("Order placed successfully")
                .data(response)
                .build()
        );
    }

    @PostMapping("/stripe")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> createStripeSession(
            @RequestBody OrderRequest request,
            @RequestHeader("Origin") String origin) {
        try {
            Long userId = getCurrentUserId();
            String sessionUrl = orderService.createStripePaymentSession(userId, request, origin);
            
            return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Stripe session created successfully")
                .data(Map.of("session_url", sessionUrl))
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to create Stripe session: " + e.getMessage())
                    .build());
        }
    }

//    @PostMapping("/verifyStripe")
//    public ResponseEntity<ApiResponse> verifyStripePayment(
//            @RequestParam Long orderId,
//            @RequestParam boolean success) {
//        orderService.verifyStripePayment(orderId, success);
//        return ResponseEntity.ok(
//            ApiResponse.builder()
//                .success(true)
//                .message(success ? "Payment verified" : "Payment failed")
//                .build()
//        );
//    }
    
    @PostMapping("/verifyStripe")
    public ResponseEntity<ApiResponse> verifyStripePayment(
            @RequestBody StripeVerificationRequest verificationRequest) {
        orderService.verifyStripePayment(verificationRequest.getOrderId(), verificationRequest.isSuccess());
        return ResponseEntity.ok(
            ApiResponse.builder()
                .success(true)
                .message(verificationRequest.isSuccess() ? "Payment verified" : "Payment failed")
                .build()
        );
    }
    
    @GetMapping("/userorders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getUserOrders() {
        Long userId = getCurrentUserId();
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(
            ApiResponse.builder()
                .success(true)
                .message("User orders retrieved")
                .data(orders)
                .build()
        );
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(
            ApiResponse.builder()
                .success(true)
                .message("All orders retrieved")
                .data(orders)
                .build()
        );
    }

    @PostMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateOrderStatus(@RequestBody UpdateStatusRequest request) {
        orderService.updateOrderStatus(request.getOrderId(), request.getStatus());
        return ResponseEntity.ok(
            ApiResponse.builder()
                .success(true)
                .message("Order status updated")
                .build()
        );
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }
}