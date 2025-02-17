package com.ecommerce.productservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ecommerce.productservice.dto.ApiResponse;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.service.ProductService;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@Service
//public class ProductServiceImpl implements ProductService {
//    
//    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
//    private final ProductRepository productRepository;
//    private final Cloudinary cloudinary;
//
//    @Value("${cloudinary.cloud-name}")
//    private String cloudName;
//
//    @Value("${cloudinary.api-key}")
//    private String apiKey;
//
//    @Value("${cloudinary.api-secret}")
//    private String apiSecret;
//
//    public ProductServiceImpl(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
//            "cloud_name", cloudName,
//            "api_key", apiKey,
//            "api_secret", apiSecret
//        ));
//    }
//
//    @Override
//    public ApiResponse getAllProducts() {
//        try {
//            List<Product> products = productRepository.findAll();
//            return new ApiResponse(true, "Products fetched successfully", products);
//        } catch (Exception e) {
//            log.error("Error fetching products", e);
//            return new ApiResponse(false, e.getMessage());
//        }
//    }
//
//    @Override
//    public ApiResponse getProductById(Long productId) {
//        try {
//            Product product = productRepository.findById(productId).orElse(null);
//            if (product != null) {
//                return new ApiResponse(true, "Product found", product);
//            }
//            return new ApiResponse(false, "Product not found");
//        } catch (Exception e) {
//            log.error("Error fetching product", e);
//            return new ApiResponse(false, e.getMessage());
//        }
//    }
//
//    @Override
//    public ApiResponse addProduct(String name, String price, String description,
//                                String category, String sizes, String bestSeller,
//                                MultipartFile image1, MultipartFile image2,
//                                MultipartFile image3, MultipartFile image4) {
//        try {
//            List<MultipartFile> images = Arrays.asList(image1, image2, image3, image4)
//                .stream()
//                .filter(img -> img != null && !img.isEmpty())
//                .collect(Collectors.toList());
//
//            List<String> imageUrls = uploadImages(images);
//
//            Product product = new Product();
//            product.setName(name);
//            product.setDescription(description);
//            product.setPrice(Double.parseDouble(price));
//            product.setCategory(category);
//            product.setBestProduct("true".equals(bestSeller));
//            product.setSizes(Arrays.asList(sizes.split(",")));
//            product.setImage(imageUrls);
//            product.setDate(LocalDateTime.now());
//
//            productRepository.save(product);
//            return new ApiResponse(true, "Product added successfully");
//        } catch (Exception e) {
//            log.error("Error adding product", e);
//            return new ApiResponse(false, e.getMessage());
//        }
//    }
//
//    @Override
//    public ApiResponse deleteProduct(Long productId) {
//        try {
//            productRepository.deleteById(productId);
//            return new ApiResponse(true, "Product deleted successfully");
//        } catch (Exception e) {
//            log.error("Error deleting product", e);
//            return new ApiResponse(false, e.getMessage());
//        }
//    }
//
//    private List<String> uploadImages(List<MultipartFile> images) {
//        try {
//            return images.stream()
//                .map(this::uploadSingleImage)
//                .filter(url -> url != null)
//                .collect(Collectors.toList());
//        } catch (Exception e) {
//            log.error("Error uploading images", e);
//            return new ArrayList<>();
//        }
//    }
//
//    private String uploadSingleImage(MultipartFile image) {
//        try {
//            var uploadResult = cloudinary.uploader()
//                .upload(image.getBytes(),
//                    ObjectUtils.asMap("resource_type", "auto"));
//            return (String) uploadResult.get("secure_url");
//        } catch (Exception e) {
//            log.error("Error uploading image", e);
//            return null;
//        }
//    }
//}




@Service
public class ProductServiceImpl implements ProductService {
    
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, 
                              @Value("${cloudinary.cloud-name}") String cloudName,
                              @Value("${cloudinary.api-key}") String apiKey,
                              @Value("${cloudinary.api-secret}") String apiSecret) {
        this.productRepository = productRepository;
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret
        ));
    }

    @PostConstruct
    public void initializeCloudinary() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret
        ));
    }

    @Override
    public ApiResponse getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return new ApiResponse(true, "Products fetched successfully", products);
        } catch (Exception e) {
            log.error("Error fetching products", e);
            return new ApiResponse(false, e.getMessage());
        }
    }

    @Override
    public ApiResponse getProductById(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                .orElse(null);
            if (product != null) {
                return new ApiResponse(true, "Product found", product);
            }
            return new ApiResponse(false, "Product not found");
        } catch (Exception e) {
            log.error("Error fetching product", e);
            return new ApiResponse(false, e.getMessage());
        }
    }

    @Override
    public ApiResponse addProduct(String name, String price, String description,
                                String category, String sizes, String bestSeller,
                                MultipartFile image1, MultipartFile image2,
                                MultipartFile image3, MultipartFile image4) {
        try {
            List<MultipartFile> images = Arrays.asList(image1, image2, image3, image4)
                .stream()
                .filter(img -> img != null && !img.isEmpty())
                .collect(Collectors.toList());

            List<String> imageUrls = uploadImages(images);

            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(Double.parseDouble(price));
            product.setCategory(category);
            product.setBestProduct("true".equalsIgnoreCase(bestSeller));
            product.setSizes(Arrays.asList(sizes.split(",")));
            product.setImage(imageUrls);
            
            product.setDate(LocalDateTime.now());

            productRepository.save(product);
            return new ApiResponse(true, "Product added successfully");
        } catch (Exception e) {
            log.error("Error adding product", e);
            return new ApiResponse(false, e.getMessage());
        }
    }

//    @Override
//    public ApiResponse deleteProduct(Long productId) {
//        try {
//            productRepository.deleteById(productId);
//            return new ApiResponse(true, "Product deleted successfully");
//        } catch (Exception e) {
//            log.error("Error deleting product", e);
//            return new ApiResponse(false, e.getMessage());
//        }
//    }

    @Override
    public ApiResponse deleteProduct(Long productId) {
        try {
            if (!productRepository.existsById(productId)) {
                return new ApiResponse(false, "Product not found");
            }
            productRepository.deleteById(productId);
            return new ApiResponse(true, "Product deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting product", e);
            return new ApiResponse(false, "Failed to delete product");
        }
    }
    
    private List<String> uploadImages(List<MultipartFile> images) {
        return images.stream()
            .map(this::uploadSingleImage)
            .filter(url -> url != null)
            .collect(Collectors.toList());
    }

    private String uploadSingleImage(MultipartFile image) {
        try {
            var uploadResult = cloudinary.uploader()
                .upload(image.getBytes(),
                    ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "ecommerce_products"
                    ));
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            log.error("Error uploading image", e);
            return null;
        }
    }
}