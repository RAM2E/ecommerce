package com.ecommerce.productservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.Base64;

@Component
public class JwtFilter extends OncePerRequestFilter {
    
    private static final Logger logger = Logger.getLogger(JwtFilter.class.getName());
    private final SecretKey key;

    public JwtFilter(@Value("${jwt.secret}") String secretKey) {
        try {
            // Use exact same Base64 encoded key as in User Service
            String normalizedKey = secretKey.trim();
            byte[] decodedKey = Base64.getDecoder().decode(normalizedKey);
            this.key = Keys.hmacShaKeyFor(decodedKey);
            logger.info("JWT Filter initialized with key");
        } catch (Exception e) {
            logger.severe("JWT initialization failed: " + e.getMessage());
            throw new IllegalStateException("Could not initialize JWT key", e);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
            HttpServletResponse response, FilterChain chain) 
            throws ServletException, IOException {
        
        try {
            String token = extractToken(request);
            if (token != null) {
                logger.info("Validating token for URI: " + request.getRequestURI());
                Claims claims = validateToken(token);
                setAuthentication(claims);
                logger.info("Token validated successfully");
            }
        } catch (Exception e) {
            logger.warning("Token validation failed: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }
        
        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            logger.info("Token extracted successfully");
            return token;
        }
        return null;
    }

    private Claims validateToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private void setAuthentication(Claims claims) {
        String role = claims.get("role", String.class);
        UsernamePasswordAuthenticationToken auth = 
            new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                List.of(new SimpleGrantedAuthority(role))
            );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}