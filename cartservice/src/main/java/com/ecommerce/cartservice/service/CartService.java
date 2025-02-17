package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.dto.CartRequest;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<?> addToCart(CartRequest request);
    ResponseEntity<?> updateCart(CartRequest request);
    ResponseEntity<?> getUserCart(Long userId); 
    ResponseEntity<?> clearUserCart(Long userId);
}