//package com.ecommerce.productservice.controller;
//
//import com.ecommerce.productservice.dto.ApiResponse;
//import com.ecommerce.productservice.service.ProductService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.security.access.prepost.PreAuthorize;
//
//@RestController
//@RequestMapping("/api/product")
//public class ProductController {
//    
//    private final ProductService productService;
//
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }
//
//    @GetMapping("/list")
//    public ResponseEntity<ApiResponse> listProducts() {
//        return ResponseEntity.ok(productService.getAllProducts());
//    }
//
//    @PostMapping("/add")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<ApiResponse> addProduct(
//            @RequestParam("name") String name,
//            @RequestParam("price") String price,
//            @RequestParam("description") String description,
//            @RequestParam("category") String category,
//            @RequestParam("sizes") String sizes,
//            @RequestParam("bestSeller") String bestSeller,
//            @RequestParam(value = "image1", required = false) MultipartFile image1,
//            @RequestParam(value = "image2", required = false) MultipartFile image2,
//            @RequestParam(value = "image3", required = false) MultipartFile image3,
//            @RequestParam(value = "image4", required = false) MultipartFile image4) {
//        
//        return ResponseEntity.ok(productService.addProduct(name, price, description, 
//            category, sizes, bestSeller, image1, image2, image3, image4));
//    }
//
//    @PostMapping("/remove")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<ApiResponse> removeProduct(@RequestParam("_id") Long productId) {
//        return ResponseEntity.ok(productService.deleteProduct(productId));
//    }
//
//    @GetMapping("/single")
//    public ResponseEntity<ApiResponse> singleProduct(@RequestParam("productId") Long productId) {
//        return ResponseEntity.ok(productService.getProductById(productId));
//    }
//}

package com.ecommerce.productservice.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.productservice.dto.ApiResponse;
import com.ecommerce.productservice.dto.SingleProductRequest;
import com.ecommerce.productservice.service.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/product")
@Validated
public class ProductController {
    
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listProducts() {
        log.debug("Fetching all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> addProduct(
            @RequestParam("name") @NotBlank String name,
            @RequestParam("price") @NotBlank String price,
            @RequestParam("description") @NotBlank String description,
            @RequestParam("category") @NotBlank String category,
            @RequestParam("sizes") @NotBlank String sizes,
            @RequestParam("bestSeller") String bestSeller,
            @RequestParam(value = "image1", required = false) MultipartFile image1,
            @RequestParam(value = "image2", required = false) MultipartFile image2,
            @RequestParam(value = "image3", required = false) MultipartFile image3,
            @RequestParam(value = "image4", required = false) MultipartFile image4) {
        
        log.debug("Adding new product: {}", name);
        return ResponseEntity.ok(productService.addProduct(
            name, price, description, category, 
            sizes, bestSeller, image1, image2, image3, image4));
    }


    @PostMapping("/remove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> removeProduct(@Valid @RequestBody SingleProductRequest request) {
        log.debug("Removing product with id: {}", request.get_id());
        return ResponseEntity.ok(productService.deleteProduct(request.get_id()));
    }

    
    @GetMapping("/single")
    public ResponseEntity<ApiResponse> singleProduct(@Valid @RequestBody SingleProductRequest request) {
        log.debug("Fetching product with id: {}", request.get_id());
        return ResponseEntity.ok(productService.getProductById(request.get_id()));
    }
}