package com.ecommerce.cartservice.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Version
    private Long version;

    public void addItem(CartItem newItem) {
        for (CartItem existingItem : items) {
            if (existingItem.equals(newItem)) {
                existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        newItem.setCart(this); // Ensure cart reference is set
        items.add(newItem);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
