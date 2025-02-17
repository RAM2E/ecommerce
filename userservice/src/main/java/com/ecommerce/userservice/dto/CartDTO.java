package com.ecommerce.userservice.dto;

import java.util.Map;

public class CartDTO {
    private Long userId;
    private Map<String, Map<String, Integer>> cartItems;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Map<String, Map<String, Integer>> getCartItems() {
		return cartItems;
	}
	public void setCartItems(Map<String, Map<String, Integer>> cartItems) {
		this.cartItems = cartItems;
	}
	public CartDTO(Long userId, Map<String, Map<String, Integer>> cartItems) {
		this.userId = userId;
		this.cartItems = cartItems;
	}
    
	public CartDTO() {
		
	}
    // itemId → {size → quantity}
}