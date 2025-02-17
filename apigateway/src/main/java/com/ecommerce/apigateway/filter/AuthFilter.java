////package com.ecommerce.apigateway.filter;
////
////import org.springframework.cloud.gateway.filter.GatewayFilter;
////import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.server.reactive.ServerHttpRequest;
////import org.springframework.http.server.reactive.ServerHttpResponse;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.stereotype.Component;
////import org.springframework.web.server.ServerWebExchange;
////import reactor.core.publisher.Mono;
////import com.fasterxml.jackson.core.JsonProcessingException;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import io.jsonwebtoken.Claims;
////import io.jsonwebtoken.Jwts;
////import io.jsonwebtoken.security.Keys;
////import jakarta.annotation.PostConstruct;
////import javax.crypto.SecretKey;
////import java.nio.charset.StandardCharsets;
////import java.util.HashMap;
////import java.util.Map;
////import static com.ecommerce.apigateway.config.Constants.*;
////
////@Component
////public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
////
////    @Value("${jwt.secret}")
////    private String secretKeyString;
////    
////    private final ObjectMapper objectMapper = new ObjectMapper();
////    private SecretKey secretKey;
////
////    public static class Config {
////        private boolean includeHeaders = true;
////        public boolean isIncludeHeaders() { return includeHeaders; }
////        public void setIncludeHeaders(boolean includeHeaders) { 
////            this.includeHeaders = includeHeaders; 
////        }
////    }
////
////    public AuthFilter() {
////        super(Config.class);
////    }
////
////    @PostConstruct
////    public void init() {
////        if (secretKeyString.length() < 32) {
////            throw new RuntimeException("JWT secret key must be at least 32 characters");
////        }
////        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
////    }
////
////    @Override
////    public GatewayFilter apply(Config config) {
////        return (exchange, chain) -> {
////            String path = exchange.getRequest().getPath().value();
////            String token = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);
////            
////            
////            if (isPublicPath(path)) {
////                return chain.filter(exchange);
////            }
////            
////            if (token == null || !token.startsWith(BEARER_PREFIX)) {
////                return onError(exchange, AUTH_REQUIRED, HttpStatus.UNAUTHORIZED);
////            }
////            
////            try {
////                Claims claims = extractClaims(token);
////                String role = claims.get(ROLE_CLAIM, String.class);
////                String userId = claims.get("userId", String.class);
////                
////                if (isAdminPath(path) && !ROLE_ADMIN.equals(role)) {
////                    return onError(exchange, ADMIN_ACCESS_REQUIRED, HttpStatus.FORBIDDEN);
////                }
////                
////                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
////                    .header("X-User-Id", userId)
////                    .header("X-User-Role", role)
////                    .header(AUTH_HEADER, token)
////                    .build();
////                
////                return chain.filter(exchange.mutate().request(modifiedRequest).build());
////                
////            } catch (Exception e) {
////                return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
////            }
////        };
////    }
////    
////    private boolean isPublicPath(String path) {
////        return path.startsWith(API_AUTH_PREFIX + "login") || 
////               path.startsWith(API_AUTH_PREFIX + "register") ||
////               path.startsWith(API_PRODUCTS_PREFIX + "list") || 
////               path.startsWith(API_PRODUCTS_PREFIX + "single");
////    }
////    
////    private boolean isAdminPath(String path) {
////        return path.startsWith(API_PRODUCTS_PREFIX + "add") || 
////               path.startsWith(API_PRODUCTS_PREFIX + "remove") ||
////               path.startsWith(API_ORDERS_PREFIX + "list") || 
////               path.startsWith(API_ORDERS_PREFIX + "status") ||
////               path.startsWith(API_AUTH_PREFIX + "admin");
////    }
////
////    private Claims extractClaims(String token) {
////        try {
////            return Jwts.parserBuilder()
////                .setSigningKey(secretKey)
////                .build()
////                .parseClaimsJws(token.substring(BEARER_PREFIX.length()))
////                .getBody();
////        } catch (Exception e) {
////            throw new RuntimeException("Invalid token");
////        }
////    }
////
////    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
////        ServerHttpResponse response = exchange.getResponse();
////        response.setStatusCode(status);
////        response.getHeaders().add("Content-Type", "application/json");
////
////        Map<String, Object> errorResponse = new HashMap<>();
////        errorResponse.put("success", false);
////        errorResponse.put("message", message);
////        errorResponse.put("status", status.value());
////        errorResponse.put("path", exchange.getRequest().getPath().value());
////
////        try {
////            String json = objectMapper.writeValueAsString(errorResponse);
////            return response.writeWith(Mono.just(response.bufferFactory()
////                .wrap(json.getBytes(StandardCharsets.UTF_8))));
////        } catch (JsonProcessingException e) {
////            return response.setComplete();
////        }
////    }
////}
//
//
//




package com.ecommerce.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import static com.ecommerce.apigateway.config.Constants.*;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Value("${jwt.secret}")
    private String secretKeyString;
    private SecretKey secretKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static class Config {
        private boolean includeHeaders = true;
        public boolean isIncludeHeaders() { return includeHeaders; }
        public void setIncludeHeaders(boolean includeHeaders) { 
            this.includeHeaders = includeHeaders; 
        }
    }

    public AuthFilter() {
        super(Config.class);
    }

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().value();
            String token = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);
            
            if (isPublicPath(path)) {
                return chain.filter(exchange);
            }

            if (token == null || !token.startsWith(BEARER_PREFIX)) {
                return onError(exchange, AUTH_REQUIRED, HttpStatus.UNAUTHORIZED);
            }

            try {
                Claims claims = extractClaims(token);
                String role = claims.get(ROLE_CLAIM, String.class);
                String userId = claims.get("userId", String.class);

                if (isAdminPath(path) && !ROLE_ADMIN.equals(role)) {
                    return onError(exchange, ADMIN_ACCESS_REQUIRED, HttpStatus.FORBIDDEN);
                }

                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Role", role)
                    .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (Exception e) {
                return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private boolean isPublicPath(String path) {
        return path.startsWith(API_AUTH_PREFIX + "login") || 
               path.startsWith(API_AUTH_PREFIX + "register") ||
               path.startsWith(API_AUTH_PREFIX + "admin") ||
               path.startsWith(API_PRODUCTS_PREFIX + "list") || 
               path.startsWith(API_PRODUCTS_PREFIX + "single");
    }

    private boolean isAdminPath(String path) {
        return path.startsWith(API_PRODUCTS_PREFIX + "add") || 
               path.startsWith(API_PRODUCTS_PREFIX + "remove") ||
               path.startsWith(API_ORDERS_PREFIX + "list") || 
               path.startsWith(API_ORDERS_PREFIX + "status");
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token.substring(BEARER_PREFIX.length()))
            .getBody();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        errorResponse.put("status", status.value());
        errorResponse.put("path", exchange.getRequest().getPath().value());

        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            return response.writeWith(Mono.just(response.bufferFactory()
                .wrap(json.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            return response.setComplete();
        }
    }
}





//package com.ecommerce.apigateway.filter;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//
//import static com.ecommerce.apigateway.config.Constants.*;
//
//@Component
//public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
//
//	@Value("${jwt.secret}")
//	private String secretKeyString;
//	private SecretKey secretKey;
//	private final ObjectMapper objectMapper = new ObjectMapper();
//
//	public static class Config {
//		// Configuration properties if needed
//	}
//
//	public AuthFilter() {
//		super(Config.class);
//	}
//
//	@PostConstruct
//	public void init() {
//		// Use UTF-8 encoding for secret key conversion
//		this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
//	}
//
//	@Override
//	public GatewayFilter apply(Config config) {
//		return (exchange, chain) -> {
//			ServerHttpRequest request = exchange.getRequest();
//			String path = request.getPath().toString();
//			String authHeader = request.getHeaders().getFirst(AUTH_HEADER);
//
//			// Bypass authentication for public endpoints
//			if (isPublicEndpoint(path)) {
//				return chain.filter(exchange);
//			}
//
//			// Validate JWT presence
//			if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
//				return sendErrorResponse(exchange, "Missing authorization token", HttpStatus.UNAUTHORIZED);
//			}
//
//			try {
//				// Extract and validate token
//				String token = authHeader.substring(BEARER_PREFIX.length());
//				Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
//
//				// Extract user details
//				String userId = claims.getSubject();
//				String role = claims.get(ROLE_CLAIM, String.class);
//
//				// Validate admin access
//				if (isAdminEndpoint(path) && !ROLE_ADMIN.equals(role)) {
//					return sendErrorResponse(exchange, "Insufficient privileges", HttpStatus.FORBIDDEN);
//				}
//
//				// Add headers for downstream services
//				ServerHttpRequest modifiedRequest = request.mutate().header(X_USER_ID_HEADER, userId)
//						.header(X_USER_ROLE_HEADER, role).build();
//
//				return chain.filter(exchange.mutate().request(modifiedRequest).build());
//
//			} catch (Exception e) {
//				return sendErrorResponse(exchange, "Invalid authorization token", HttpStatus.UNAUTHORIZED);
//			}
//		};
//	}
//
//	private boolean isPublicEndpoint(String path) {
//		return path.matches("^/api/user/(login|register|admin)/?$|" + "^/api/product/(list|single)/?$");
//	}
//
//	private boolean isAdminEndpoint(String path) {
//		return path.matches("^/api/product/(add|remove)/?$|" + "^/api/order/(list|status)/?$");
//	}
//
//	private Mono<Void> sendErrorResponse(ServerWebExchange exchange, String message, HttpStatus status) {
//		ServerHttpResponse response = exchange.getResponse();
//		response.setStatusCode(status);
//
//		try {
//			String json = objectMapper.writeValueAsString(Map.of("status", status.value(), "error",
//					status.getReasonPhrase(), "message", message, "path", exchange.getRequest().getPath()));
//
//			response.getHeaders().add("Content-Type", "application/json");
//			return response.writeWith(Mono.just(response.bufferFactory().wrap(json.getBytes())));
//		} catch (Exception e) {
//			return response.setComplete();
//		}
//	}
//}