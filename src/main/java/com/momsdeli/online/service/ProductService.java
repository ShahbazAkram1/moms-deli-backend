package com.momsdeli.online.service;

import com.momsdeli.online.exception.ProductException;
import com.momsdeli.online.model.Product;
import com.momsdeli.online.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Page<Product> findAll(Pageable pageable) throws ProductException;

    Page<Product> findByCategoryId(long id, Pageable pageable) throws ProductException;

    Page<Product> findByNameContaining(String name, Pageable pageable) throws ProductException;

    Product createProduct(ProductRequest productRequest) throws ProductException;

    String deleteProduct(Long productId) throws ProductException;

    Product updateProduct(Long productId, Product product) throws ProductException;

    Product findByProductId(Long id) throws ProductException;

//    List<Product> findProductByCategory(String category) throws ProductException;

    Page<Product> getAllProduct(String category, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort,
                                String stock, Integer pageNumber, Integer pageSize) throws ProductException;


}