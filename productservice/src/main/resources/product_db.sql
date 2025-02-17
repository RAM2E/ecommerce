-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Drop existing tables
DROP TABLE IF EXISTS product_images;
DROP TABLE IF EXISTS product_sizes;
DROP TABLE IF EXISTS products;

-- Enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Create products table matching Hibernate inserts
CREATE TABLE products (
    _id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    best_product BOOLEAN DEFAULT FALSE,
    date DATETIME NOT NULL
);

-- Create product_images table matching Hibernate inserts
CREATE TABLE product_images (
    product_id BIGINT,
    image_url TEXT,
    FOREIGN KEY (product_id) 
    REFERENCES products(_id) 
    ON DELETE CASCADE
);

-- Create product_sizes table matching Hibernate inserts
CREATE TABLE product_sizes (
    product_id BIGINT,
    size VARCHAR(10),
    FOREIGN KEY (product_id) 
    REFERENCES products(_id) 
    ON DELETE CASCADE
);