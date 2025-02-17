package com.ecommerce.cartservice.service.impl;

import com.ecommerce.cartservice.client.UserServiceClient;
import com.ecommerce.cartservice.dto.CartRequest;
import com.ecommerce.cartservice.model.Cart;
import com.ecommerce.cartservice.model.CartItem;
import com.ecommerce.cartservice.repository.CartRepository;
import com.ecommerce.cartservice.service.CartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserServiceClient userServiceClient;

    public CartServiceImpl(CartRepository cartRepository, UserServiceClient userServiceClient) {
        this.cartRepository = cartRepository;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public ResponseEntity<?> addToCart(CartRequest request) {
        // Verify user exists
        ResponseEntity<?> userResponse = userServiceClient.getUser(request.getUserId());
        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(404).body("User not found");
        }

        // Get cart directly (no Optional)
        Cart cart = cartRepository.findByUserId(request.getUserId());
        
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(request.getUserId());
            cart = cartRepository.save(cart);
        }

        cart.addItem(new CartItem(request.getItemId(), request.getSize(), request.getQuantity()));
        cartRepository.save(cart);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Item added to cart"
        ));
    }

    @Override
    public ResponseEntity<?> updateCart(CartRequest request) {
        Cart cart = cartRepository.findByUserId(request.getUserId());
        if (cart == null) {
            return ResponseEntity.status(404).body(Map.of(
                "success", false,
                "message", "Cart not found"
            ));
        }

        CartItem itemToUpdate = null;
        for (CartItem item : cart.getItems()) {
            if (item.getItemId().equals(request.getItemId()) && item.getSize().equals(request.getSize())) {
                itemToUpdate = item;
                break;
            }
        }

        if (itemToUpdate == null) {
            return ResponseEntity.status(404).body(Map.of(
                "success", false,
                "message", "Item not found in cart"
            ));
        }

        if (request.getQuantity() <= 0) {
            cart.getItems().remove(itemToUpdate);
        } else {
            itemToUpdate.setQuantity(request.getQuantity());
        }

        cartRepository.save(cart);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Cart updated"
        ));
    }

    @Override
    public ResponseEntity<?> getUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        Map<String, Map<String, Integer>> cartData = new HashMap<>();

        if (cart != null) {
            for (CartItem item : cart.getItems()) {
                String itemId = item.getItemId();
                String size = item.getSize();
                int quantity = item.getQuantity();

                if (!cartData.containsKey(itemId)) {
                    cartData.put(itemId, new HashMap<>());
                }
                cartData.get(itemId).put(size, quantity);
            }
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "cartData", cartData.isEmpty() ? Collections.emptyMap() : cartData
        ));
    }
    
 // CartServiceImpl.java
    @Override
    public ResponseEntity<?> clearUserCart(Long userId) {
        try {
            Cart cart = cartRepository.findByUserId(userId);
                   
            
            cart.getItems().clear();
            cartRepository.save(cart);
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error clearing cart: " + e.getMessage());
        }
    }
}