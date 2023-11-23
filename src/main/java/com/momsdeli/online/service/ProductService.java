package com.momsdeli.online.service;

import com.momsdeli.online.model.Product;

public interface ProductService {
    Product getProductById(Long productId);
    // Other methods if needed...
}