//package com.ecommerce.apigateway.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.ecommerce.apigateway.filter.AuthFilter;
//
//@Configuration
//public class GatewayConfig {
//    
//    private final AuthFilter authFilter;
//    
//    public GatewayConfig(AuthFilter authFilter) {
//        this.authFilter = authFilter;
//    }
//    
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//            // Public Routes
//            .route("auth-public", r -> r
//                .path("/api/user/login", "/api/user/register","/api/user/admin")
//                .uri("lb://user-service"))
//                
////            .route("auth-admin", r -> r
////                .path("/api/user/admin")
////                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
////                .uri("lb://user-service"))
//            
//            // Product Routes
//            .route("product-public", r -> r
//                .path("/api/product/list", "/api/product/single")
//                .uri("lb://product-service"))
//                
//            .route("product-admin", r -> r
//                .path("/api/product/add", "/api/product/remove")
//                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
//                .uri("lb://product-service"))
//                
//            // Cart Routes    
//            .route("cart-user", r -> r
//                .path("/api/cart/**")
//                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
//                .uri("lb://cart-service"))
//                
//            // Order Routes - User
//            .route("order-user", r -> r
//                .path("/api/order/place", 
//                      "/api/order/stripe", 
//                      "/api/order/razorpay", 
//                      "/api/order/verifyStripe", 
//                      "/api/order/userorders")
//                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
//                .uri("lb://order-service"))
//                
//            // Order Routes - Admin
//            .route("order-admin", r -> r
//                .path("/api/order/list", "/api/order/status")
//                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
//                .uri("lb://order-service"))
//                
//            .build();
//    }
//}


package com.ecommerce.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ecommerce.apigateway.filter.AuthFilter;

@Configuration
public class GatewayConfig {
    
    private final AuthFilter authFilter;
    
    public GatewayConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Auth Routes
            .route("auth-public", r -> r
                .path("/api/user/login", 
                      "/api/user/register",
                      "/api/user/admin")
                .uri("lb://user-service"))
            
            .route("auth-user", r -> r
                    .path("/api/user/**")
                    .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                    .uri("lb://user-service"))
            
            // Product Routes
            .route("product-public", r -> r
                .path("/api/product/list", 
                      "/api/product/single")
                .uri("lb://product-service"))
                
            .route("product-admin", r -> r
                .path("/api/product/add", 
                      "/api/product/remove")
                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                .uri("lb://product-service"))
                
            // Cart Routes
            .route("cart", r -> r
                .path("/api/cart/**")
                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                .uri("lb://cart-service"))
                
            // Order Routes
            .route("order-user", r -> r
                .path("/api/order/place", 
                      "/api/order/stripe", 
                      "/api/order/razorpay", 
                      "/api/order/verifyStripe", 
                      "/api/order/userorders")
                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                .uri("lb://order-service"))
                
            .route("order-admin", r -> r
                .path("/api/order/list", 
                      "/api/order/status")
                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                .uri("lb://order-service"))
                
            .build();
    }
}




//package com.ecommerce.apigateway.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.ecommerce.apigateway.filter.AuthFilter;
//
//@Configuration
//public class GatewayConfig {
//    
//    private final AuthFilter authFilter;
//    
//    public GatewayConfig(AuthFilter authFilter) {
//        this.authFilter = authFilter;
//    }
//    
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//            // Auth Service Routes
//            .route("auth-public", r -> r
//                .path(
//                    "/api/user/login", 
//                    "/api/user/register",
//                    "/api/user/admin"
//                )
//                .uri("lb://user-service"))
//            
//            // Protected User Routes
//            .route("auth-user", r -> r
//                .path("/api/user/**")
//                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
//                .uri("lb://user-service"))
//            
//            // Product Service Routes
//            .route("product-public", r -> r
//                .path(
//                    "/api/product/list", 
//                    "/api/product/single"
//                )
//                .uri("lb://product-service"))
//                
//            .route("product-admin", r -> r
//            	    .path("/api/product/add", "/api/product/remove")
//            	    .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
//            	    .uri("lb://product-service"))
//                
//            // Cart Service Routes
//            .route("cart-service", r -> r
//                .path("/api/cart/**")
//                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
//                .uri("lb://cart-service"))
//                
//            // Order Service Routes
//            .route("order-user-actions", r -> r
//                .path(
//                    "/api/order/place", 
//                    "/api/order/stripe", 
//                    "/api/order/razorpay", 
//                    "/api/order/verify", 
//                    "/api/order/userorders"
//                )
//                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
//                .uri("lb://order-service"))
//                
//            .route("order-admin-actions", r -> r
//                .path(
//                    "/api/order/list", 
//                    "/api/order/status"
//                )
//                .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
//                .uri("lb://order-service"))
//                
//            .build();
//    }
//}