package com.ecommerce.apigateway.config;

public class Constants {
    // Role Constants - match exactly with tokens
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    
    // Authorization Constants
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    
    // API Endpoint Prefixes
    public static final String API_AUTH_PREFIX = "/api/auth/";
    public static final String API_PRODUCTS_PREFIX = "/api/products/";
    public static final String API_ORDERS_PREFIX = "/api/orders/";
    public static final String API_CART_PREFIX = "/api/cart/";
    
    // Error Messages
    public static final String ADMIN_ACCESS_REQUIRED = "Admin access required";
    public static final String AUTH_REQUIRED = "Authentication required";
    public static final String INVALID_TOKEN = "Invalid or expired token";
    public static final String SERVER_ERROR = "Internal server error";
    
    // Token Claims
    public static final String ROLE_CLAIM = "role";
    public static final String USER_ID_CLAIM = "userId";
    public static final String EMAIL_CLAIM = "email";
    public static final String TOKEN_EXPIRY = "exp";
    public static final String TOKEN_ISSUED = "iat";
    
    // HTTP Headers
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    
    // Response Keys
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String ERROR_KEY = "error";
    
    

    public static final String X_USER_ID_HEADER = "X-User-Id";
    public static final String X_USER_ROLE_HEADER = "X-User-Role";
}