package com.ecommerce.orderservice.dto;

public class OrderItemDto {
	private Long productId;
	private String name;
	private Double price;
	private Integer quantity;
	private String size;
	private String image;

	
	

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public OrderItemDto(Long productId, String name, Double price, Integer quantity, String size,String image) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.size = size;
		this.image = image;
	}

	// Getters & Setters
}