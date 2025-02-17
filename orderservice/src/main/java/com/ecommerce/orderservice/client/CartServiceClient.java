package com.ecommerce.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service", configuration = FeignClientConfig.class)
public interface CartServiceClient {
    
    @DeleteMapping("/api/cart/{userId}")
    ResponseEntity<?> clearCart(@PathVariable Long userId);
}