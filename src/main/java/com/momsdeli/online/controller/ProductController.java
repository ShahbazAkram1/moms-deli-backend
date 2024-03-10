/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 1/3/2024$
 * Time: 8:08 PM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.controller;

import com.momsdeli.online.exception.CategoryNotFoundException;
import com.momsdeli.online.exception.ProductNotFoundException;
import com.momsdeli.online.model.ProductCategory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import com.momsdeli.online.model.Product;
import com.momsdeli.online.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/")
    public Page<Product> getAllProducts(@RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size) {
        // Check if page and size are null, and provide defaults if necessary
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 10; // Provide a default size, e.g., 10

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return productService.findAllProducts(pageRequest);
    }

    @GetMapping("/category")
    public ResponseEntity<Page<Product>> getAllProductsByCategory(@RequestParam("categoryId") Long categoryId,
                                                                  @RequestParam(required = false) int page,
                                                                  @RequestParam(required = false) int size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Product> products = productService.findAllProductsByCategory(categoryId, pageRequest);
            if (products.isEmpty()) {
                throw new ProductNotFoundException("No products found for category with ID: " + categoryId);
            }
            log.info("Products retrieved for category with ID {}:", categoryId);
            return ResponseEntity.ok(products);
        } catch (ProductNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
        } catch (Exception e) {
            log.error("Error in getAllProductsByCategory", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Page.empty());
        }
    }

    @GetMapping("/categoryByName")
    public ResponseEntity<Page<Product>> getAllProductsByCategoryName(@RequestParam("categoryName") String categoryName,
                                                                      @RequestParam(required = false) int page,
                                                                      @RequestParam(required = false) int size) {
        try {
            if (StringUtils.isBlank(categoryName)) {
                throw new IllegalArgumentException("Category name cannot be null or empty");
            }

            PageRequest pageRequest = PageRequest.of(page, size);
            ProductCategory category = (ProductCategory) productService.findCategoryByName(categoryName, pageRequest);
            if (category == null) {
                throw new CategoryNotFoundException("Category not found");
            }

            Page<Product> products = productService.findAllProductsByCategory(category.getId(), pageRequest);
            if (products.isEmpty()) {
                throw new ProductNotFoundException("No products found for category with name: " + categoryName);
            }

            return ResponseEntity.ok(products);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Page.empty());
        } catch (CategoryNotFoundException | ProductNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
        } catch (Exception e) {
            log.error("Error in getAllProductsByCategory", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Page.empty());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        System.out.println(product.getCategory().getId());
        return productService.saveProduct(product);
    }

    @PutMapping("/")
    public ResponseEntity<Product> updateProduct(@RequestBody Product productDetails) {
        Product product = productService.findProductById(productDetails.getId());

        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setImageUrl(productDetails.getImageUrl());
        product.setActive(productDetails.isActive());
        product.setUnitsInStock(productDetails.getUnitsInStock());
        product.setCategory(productDetails.getCategory());
        product.setAdditionalItems(productDetails.getAdditionalItems());

        Product updatedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProductsByName(String keyword, int page, int size) {
        if (StringUtils.isBlank(keyword)) {
            return ResponseEntity.badRequest().build();
        }

        Page<Product> products = productService.searchProductsByName(keyword, page, size);
        if (products.isEmpty()) {
            String message = "No products found for keyword: " + keyword;
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", message));
        }

        return ResponseEntity.ok(products);
    }


}
