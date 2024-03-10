package com.momsdeli.online.service.impl;

import com.momsdeli.online.exception.CategoryNotFoundException;
import com.momsdeli.online.exception.InvalidCategoryNameException;
import com.momsdeli.online.model.Product;
import com.momsdeli.online.model.ProductCategory;
import com.momsdeli.online.repository.ProductRepository;
import com.momsdeli.online.service.CategoryService;
import com.momsdeli.online.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;

    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    @Transactional
    public List<Product> findAllProducts() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        Collections.shuffle(products);
        return products;
    }

    @Override
    @Transactional
    public Product findProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        log.info("Saving product: {}", product);
        System.out.println(product);
        ProductCategory category = this.categoryService.findById(product.getCategory().getId());
//        ProductCategory category = this.categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new RuntimeException("Category Not Found"));
        if (category != null) {
            product.setCategory(category);
            return productRepository.save(product);
        } else {
            return null;
        }
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        this.productRepository.findById(id).ifPresent(productRepository::delete);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public Page<Product> findAllProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findAllProductsByCategory(Long categoryId, Pageable pageable) {
        return this.productRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> findCategoryByName(String categoryName, Pageable pageable) {

        if (StringUtils.isBlank(categoryName)) {
            throw new InvalidCategoryNameException("Category name cannot be null or empty");
        }
        // find category by name
        ProductCategory category = (ProductCategory) categoryService.findByName(categoryName);

        if (StringUtils.isBlank(categoryName)) {

            throw new CategoryNotFoundException("Category not found ");
        }

        return productRepository.findByCategoryName(category.getName(), pageable);
    }

    @Override
    public Page<Product> searchProductsByName(String keyword, int page, int size) {
        if (StringUtils.isBlank(keyword)) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        log.info("Searching products by name containing '{}'", keyword);
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> searchResults = productRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
        log.info("Found {} products matching the search criteria", searchResults.getTotalElements());
        return searchResults;
    }


}