package com.ecommerce.productservice.dto;


import jakarta.validation.constraints.NotNull;

public class SingleProductRequest {
    @NotNull(message = "Product ID is required")
    private Long _id;

    public SingleProductRequest() {}
    
    public SingleProductRequest(Long _id) {
        this._id = _id;
    }

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

    
}