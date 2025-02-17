
package com.ecommerce.userservice.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.userservice.dto.AuthResponse;
import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.RegisterRequest;

import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.security.JwtService;
import com.ecommerce.userservice.service.UserService;
import com.ecommerce.userservice.constants.UserRole;

@Service

@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${admin.email}")
    private String adminEmail;
    
    @Value("${admin.password}")
    private String adminPassword;
    
    private String encodedAdminPassword;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    
    public UserServiceImpl(UserRepository userRepository, 
                         PasswordEncoder passwordEncoder, 
                         JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostConstruct
    public void init() {
        this.encodedAdminPassword = passwordEncoder.encode(adminPassword);
    }

    @Override
    public ResponseEntity<?> loginUser(LoginRequest request) {
        log.info("Processing login for user: {}", request.getEmail());
        try {
            User user = userRepository.findByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AuthResponse.builder()
                        .success(false)
                        .message("User not found")
                        .build());
            }

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtService.generateUserToken(user.getId().toString());
                return ResponseEntity.ok(AuthResponse.builder()
                    .success(true)
                    .message("Login Successful")
                    .token(token)
                    .role(UserRole.USER)
                    .userId(user.getId())
                    .build());
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse.builder()
                    .success(false)
                    .message("Invalid credentials")
                    .build());
        } catch (Exception e) {
            log.error("Login error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthResponse.builder()
                    .success(false)
                    .message("Login failed")
                    .build());
        }
    }

    @Override
    public ResponseEntity<?> registerUser(RegisterRequest request) {
        log.info("Processing registration for user: {}", request.getEmail());
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(AuthResponse.builder()
                        .success(false)
                        .message("Email already exists")
                        .build());
            }

            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            
            user = userRepository.save(user);
            String token = jwtService.generateUserToken(user.getId().toString());
            
            return ResponseEntity.ok(AuthResponse.builder()
                .success(true)
                .message("Registration successful")
                .token(token)
                .role(UserRole.USER)
                .userId(user.getId())
                .build());
        } catch (Exception e) {
            log.error("Registration error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthResponse.builder()
                    .success(false)
                    .message("Registration failed")
                    .build());
        }
    }

    @Override
    public ResponseEntity<?> adminLogin(LoginRequest request) {
        log.info("Processing admin login attempt");
        try {
            if (request.getEmail().equals(adminEmail) && 
                passwordEncoder.matches(request.getPassword(), encodedAdminPassword)) {
                
                String token = jwtService.generateAdminToken(adminEmail);
                return ResponseEntity.ok(AuthResponse.builder()
                    .success(true)
                    .message("Admin login successful")
                    .token(token)
                    .role(UserRole.ADMIN)
                    .build());
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse.builder()
                    .success(false)
                    .message("Invalid admin credentials")
                    .build());
        } catch (Exception e) {
            log.error("Admin login error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthResponse.builder()
                    .success(false)
                    .message("Admin login failed")
                    .build());
        }
    }

    @Override
    public ResponseEntity<?> getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", user.getId());
                    response.put("name", user.getName());
                    response.put("email", user.getEmail());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "User not found with id: " + userId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }


}



