package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ApiResponse getAllProducts();
    ApiResponse getProductById(Long productId);
    ApiResponse addProduct(String name, String price, String description, 
                         String category, String sizes, String bestSeller,
                         MultipartFile image1, MultipartFile image2, 
                         MultipartFile image3, MultipartFile image4);
    ApiResponse deleteProduct(Long productId);
}