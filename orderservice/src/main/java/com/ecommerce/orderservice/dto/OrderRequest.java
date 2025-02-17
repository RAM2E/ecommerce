package com.ecommerce.orderservice.dto;


import java.util.List;

public class OrderRequest {
    private List<OrderItemDto> items;
    private Double amount;
    private AddressDto address;
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
	
    
    // Getters & Setters
}