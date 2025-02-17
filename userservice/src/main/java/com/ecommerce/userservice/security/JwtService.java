package com.ecommerce.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class JwtService {
    
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    
    @Value("${jwt.secret}")
    private String secretKey;

    // For regular users
    public String generateUserToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", ROLE_USER);
        claims.put("userId", userId);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // For admin users
    public String generateAdminToken(String adminEmail) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", ROLE_ADMIN);
        claims.put("email", adminEmail);
        claims.put("userId", "ADMIN");
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(adminEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
        String role = claims.get("role", String.class);
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public boolean isAdmin(Claims claims) {
        return ROLE_ADMIN.equals(claims.get("role", String.class));
    }

    public boolean isUser(Claims claims) {
        return ROLE_USER.equals(claims.get("role", String.class));
    }

    public String getUserIdFromToken(Claims claims) {
        return claims.get("userId", String.class);
    }
}