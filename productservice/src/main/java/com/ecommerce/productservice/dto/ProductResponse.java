package com.ecommerce.productservice.dto;

public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private String imageUrl;

    // Private constructor for builder
    private ProductResponse() {}

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }

    // Builder class
    public static class Builder {
        private ProductResponse productResponse;

        public Builder() {
            productResponse = new ProductResponse();
        }

        public Builder id(String id) {
            productResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            productResponse.name = name;
            return this;
        }

        public Builder description(String description) {
            productResponse.description = description;
            return this;
        }

        public Builder price(Double price) {
            productResponse.price = price;
            return this;
        }

        public Builder category(String category) {
            productResponse.category = category;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            productResponse.imageUrl = imageUrl;
            return this;
        }

        public ProductResponse build() {
            return productResponse;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}