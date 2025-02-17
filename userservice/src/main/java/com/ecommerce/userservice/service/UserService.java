//package com.ecommerce.userservice.service;
//
//import com.ecommerce.userservice.dto.LoginRequest;
//import com.ecommerce.userservice.dto.RegisterRequest;
//import org.springframework.http.ResponseEntity;
//
//public interface UserService {
//    ResponseEntity<?> loginUser(LoginRequest request);
//    ResponseEntity<?> registerUser(RegisterRequest request);
//    ResponseEntity<?> adminLogin(LoginRequest request);
//	ResponseEntity<?> getCartData(Long userId);
//	ResponseEntity<?> updateCartData(Long userId, CartRequest cartData);
//}


package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> loginUser(LoginRequest request);
    ResponseEntity<?> registerUser(RegisterRequest request);
    ResponseEntity<?> adminLogin(LoginRequest request);
    ResponseEntity<?> getUserById(Long userId);
}
