package com.ecommerce.cartservice.dto;

import java.util.Map;


public class UserCartUpdateRequest {
    private Map<String, Map<String, Integer>> cartItems;

	public Map<String, Map<String, Integer>> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Map<String, Map<String, Integer>> cartItems) {
		this.cartItems = cartItems;
	}
    
}