package com.momsdeli.online.service;

import com.momsdeli.online.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<Product> findAllProducts();

    Product findProductById(Long id);

    Product saveProduct(Product product);

    void deleteProduct(Long id);

    Product getProductById(Long productId);

    Page<Product> findAllProducts(Pageable pageable);

    Page<Product> findAllProductsByCategory(Long categoryId, Pageable pageable);

    Page<Product> findCategoryByName(String categoryName, Pageable pageable);

    Page<Product> searchProductsByName(String keyword, int page, int size);
}