package com.ecommerce.userservice.dto;

import com.ecommerce.userservice.constants.UserRole;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private UserRole role;
    private Long userId;

    // Default Constructor
    public AuthResponse() {
    }

    // Parameterized Constructor
    public AuthResponse(boolean success, String message, String token, UserRole role, Long userId) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.role = role;
        this.userId = userId;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Builder Pattern
    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private boolean success;
        private String message;
        private String token;
        private UserRole role;
        private Long userId;

        AuthResponseBuilder() {
        }

        public AuthResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public AuthResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthResponseBuilder role(UserRole role) {
            this.role = role;
            return this;
        }

        public AuthResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(success, message, token, role, userId);
        }
    }
}