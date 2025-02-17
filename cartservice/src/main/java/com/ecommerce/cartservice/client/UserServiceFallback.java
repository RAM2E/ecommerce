//package com.ecommerce.cartservice.client;
//
//import com.ecommerce.cartservice.model.CartData;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserServiceFallback implements UserServiceClient {
//    private static final Logger log = LoggerFactory.getLogger(UserServiceFallback.class);
//
//    @Override
//    public CartData getCartData(Long userId) {
//        if (userId == null) {
//            log.error("Fallback: User ID is null");
//            return new CartData();
//        }
//        log.warn("Fallback: Unable to fetch cart data for user {}", userId);
//        CartData emptyCart = new CartData();
//        emptyCart.setUserId(userId);
//        return emptyCart;
//    }
//
//    @Override
//    public void updateCartData(Long userId, CartData cartData) {
//        if (userId == null) {
//            log.error("Fallback: User ID is null");
//            return;
//        }
//        if (cartData == null) {
//            log.error("Fallback: Cart data is null for user {}", userId);
//            return;
//        }
//        log.warn("Fallback: Unable to update cart for user {}. Cart items: {}", 
//                userId, cartData.getCartItems() != null ? cartData.getCartItems().size() : 0);
//    }
//}


//package com.ecommerce.cartservice.client;
//
//import com.ecommerce.cartservice.model.CartData;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserServiceFallback implements UserServiceClient {
//    private static final Logger log = LoggerFactory.getLogger(UserServiceFallback.class);
//
//    @Override
//    public CartData getCartData(Long userId) {
//        if (userId == null) {
//            log.error("Fallback: User ID is null");
//            CartData emptyCart = new CartData();
//            emptyCart.setUserId(null);
//            return emptyCart;
//        }
//        log.warn("Fallback: Unable to fetch cart data for user {}", userId);
//        CartData cart = new CartData();
//        cart.setUserId(userId);
//        return cart;
//    }
//
//    @Override
//    public void updateCartData(Long userId, CartData cartData) {
//        if (userId == null) {
//            log.error("Fallback: User ID is null in update request");
//            return;
//        }
//        if (cartData == null) {
//            log.error("Fallback: Cart data is null for user {}", userId);
//            return;
//        }
//        log.warn("Fallback: Unable to update cart for user {}", userId);
//    }
//}


//
//package com.ecommerce.cartservice.client;
//
//import com.ecommerce.cartservice.model.CartData;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserServiceFallback implements UserServiceClient {
//    private static final Logger log = LoggerFactory.getLogger(UserServiceFallback.class);
//
//    @Override
//    public CartData getCartData(Long userId, String token) {
//        if (userId == null) {
//            log.error("Fallback: User ID is null in getCartData request");
//            return new CartData();
//        }
//        if (token == null) {
//            log.error("Fallback: Authorization token is null for user {}", userId);
//            return new CartData();
//        }
//        
//        log.warn("Fallback: Unable to fetch cart data for user {}", userId);
//        CartData cart = new CartData();
//        cart.setUserId(userId);
//        return cart;
//    }
//
//    @Override
//    public void updateCartData(Long userId, String token, CartData cartData) {
//        if (userId == null || token == null) {
//            log.error("Fallback: Missing required fields - userId: {}, hasToken: {}", 
//                     userId, token != null);
//            return;
//        }
//        if (cartData == null) {
//            log.error("Fallback: Cart data is null for user {}", userId);
//            return;
//        }
//        log.warn("Fallback: Unable to update cart for user {}", userId);
//    }
//}


//
//package com.ecommerce.cartservice.client;
//
//import com.ecommerce.cartservice.dto.UserCartResponse;
//import com.ecommerce.cartservice.model.CartData;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import java.util.HashMap;
//
//@Component
//public class UserServiceFallback implements UserServiceClient {
//    private static final Logger log = LoggerFactory.getLogger(UserServiceFallback.class);
//
//    @Override
//    public CartData getCartData(Long userId, String token) {
//        if (userId == null) {
//            log.error("Fallback: User ID is null in getCartData request");
//            return new CartData();
//        }
//        log.warn("Fallback: Unable to fetch cart data for user {}", userId);
//        CartData cart = new CartData();
//        cart.setUserId(userId);
//        cart.setCartItems(new HashMap<>());
//        return cart;
//    }
//
//    @Override
//    public void updateCartData(Long userId, String token, CartData cartData) {
//        if (userId == null || token == null) {
//            log.error("Fallback: Missing required fields - userId: {}, hasToken: {}", 
//                     userId, token != null);
//            return;
//        }
//        log.warn("Fallback: Unable to update cart for user {}", userId);
//    }
//}