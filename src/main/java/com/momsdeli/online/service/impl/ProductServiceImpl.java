package com.momsdeli.online.service.impl;

import com.momsdeli.online.exception.ProductException;
import com.momsdeli.online.model.Product;
import com.momsdeli.online.repository.CategoryRepository;
import com.momsdeli.online.repository.ProductRepository;
import com.momsdeli.online.request.ProductRequest;
import com.momsdeli.online.service.ProductService;
import com.momsdeli.online.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) throws ProductException {
        try {
            return productRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Error finding all products: ", e);
            throw new ProductException("Error finding all products: " + e.getMessage());
        }
    }

    @Override
    public Page<Product> findByCategoryId(long id, Pageable pageable) throws ProductException {
        try {
            return productRepository.findByCategoryId(id, pageable);
        } catch (Exception e) {
            logger.error("Error finding products by category ID " + id, e);
            throw new ProductException("Error finding products by category ID " + id + ": " + e.getMessage());
        }
    }

    @Override
    public Page<Product> findByNameContaining(String name, Pageable pageable) throws ProductException {
        try {
            return productRepository.findByNameContaining(name, pageable);
        } catch (Exception e) {
            logger.error("Error finding products containing name " + name, e);
            throw new ProductException("Error finding products containing name " + name + ": " + e.getMessage());
        }
    }

    @Override
    public Product createProduct(ProductRequest productRequest) throws ProductException {
        try {
            Product product = new Product();
            product.setDescription(productRequest.getDescription());
            return productRepository.save(product);
        } catch (Exception e) {
            logger.error("Error creating product: ", e);
            throw new ProductException("Error creating product: " + e.getMessage());
        }
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductException("Product not found"));
            productRepository.delete(product);
            return "Product deleted successfully";
        } catch (Exception e) {
            logger.error("Error deleting product with ID " + productId, e);
            throw new ProductException("Error deleting product: " + e.getMessage());
        }
    }

    @Override
    public Product updateProduct(Long productId, Product productDetails) throws ProductException {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductException("Product not found"));
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            // Update other fields
            return productRepository.save(product);
        } catch (Exception e) {
            logger.error("Error updating product with ID " + productId, e);
            throw new ProductException("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public Product findByProductId(Long id) throws ProductException {
        try {
            return productRepository.findById(id)
                    .orElseThrow(() -> new ProductException("Product with ID " + id + " not found"));
        } catch (Exception e) {
            logger.error("Error finding product with ID " + id, e);
            throw new ProductException("Error finding product: " + e.getMessage());
        }
    }

//    @Override
//    public List<Product> findProductByCategory(String category) throws ProductException {
//        try {
//            return productRepository.findByCategoryName(category);
//        } catch (Exception e) {
//            logger.error("Error finding products by category " + category, e);
//            throw new ProductException("Error finding products by category " + category + ": " + e.getMessage());
//        }
//    }

    @Override
    public Page<Product> getAllProduct(String category, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) throws ProductException {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            return productRepository.findAll(pageable); // Modify this according to the specific criteria
        } catch (Exception e) {
            logger.error("Error getting all products", e);
            throw new ProductException("Error getting all products: " + e.getMessage());
        }
    }
}
