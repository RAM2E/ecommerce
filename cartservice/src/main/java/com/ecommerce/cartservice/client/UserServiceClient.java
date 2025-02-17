
package com.ecommerce.cartservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



 
@FeignClient(name = "user-service")
public interface UserServiceClient {
    
    @GetMapping("/api/user/{userId}")
    ResponseEntity<?> getUser(@PathVariable Long userId);
}
