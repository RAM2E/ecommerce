package com.ecommerce.cartservice.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemId;
    private String size;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    public CartItem() {} // Required for Hibernate

    public CartItem(String itemId, String size, int quantity) {
        this.itemId = itemId;
        this.size = size;
        this.quantity = quantity;
    }

    // Ensure the Cart reference is set
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    // Getters and Setters
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Cart getCart() { return cart; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(itemId, cartItem.itemId) && 
               Objects.equals(size, cartItem.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, size);
    }
}
