package com.momsdeli.online.service;

import com.momsdeli.online.exception.ProductException;
import com.momsdeli.online.model.Category;
import com.momsdeli.online.model.Product;
import com.momsdeli.online.repository.CategoryRepository;
import com.momsdeli.online.repository.ProductRepository;
import com.momsdeli.online.request.ProductRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Page<Product> findAll(Pageable pageable) {
        log.info("Fetching all products");
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findByCategoryId(long id, Pageable pageable) {
        log.info("Fetching products by category ID: {}", id);
        return productRepository.findByCategoryId(id, pageable);
    }

    @Override
    public Page<Product> findByNameContaining(String name, Pageable pageable) {
        log.info("Fetching products by name containing: {}", name);
        return productRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Product createProduct(ProductRequest productRequest) {

        Category category = new Category();
        return null;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        return null;
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public Product findByProductId(Long id) throws ProductException {
        return null;
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return null;
    }

    @Override
    public Page<Product> getAllProduct(String category, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        return null;
    }

}