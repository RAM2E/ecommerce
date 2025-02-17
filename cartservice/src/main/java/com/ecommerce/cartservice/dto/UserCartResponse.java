package com.ecommerce.cartservice.dto;

import java.util.Map;


public class UserCartResponse {
    private boolean success;
    private Map<String, Map<String, Integer>> cartItems;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Map<String, Map<String, Integer>> getCartItems() {
		return cartItems;
	}
	public void setCartItems(Map<String, Map<String, Integer>> cartItems) {
		this.cartItems = cartItems;
	}
    
}