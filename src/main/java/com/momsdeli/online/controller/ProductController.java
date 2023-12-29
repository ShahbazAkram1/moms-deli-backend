/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/25/2023$
 * Time: 2:32 PM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.controller;

import com.momsdeli.online.exception.ProductException;
import com.momsdeli.online.model.Product;
import com.momsdeli.online.request.ProductRequest;
import com.momsdeli.online.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"https://momsdelionline.com", "http://localhost:4200"})
public class ProductController {


    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<Page<Product>> listProduct(Pageable pageable) throws ProductException {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/search/findByCategoryId")
    public ResponseEntity<Page<Product>> findByCategoryId(@RequestParam("id") long id, Pageable pageable) throws ProductException {
        return ResponseEntity.ok(productService.findByCategoryId(id, pageable));
    }

    @GetMapping("/search/findByNameContaining")
    public ResponseEntity<Page<Product>> findByNameContaining(@RequestParam("name") String name, Pageable pageable) throws ProductException {
        return ResponseEntity.ok(productService.findByNameContaining(name, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) throws ProductException {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Product>> getProductsByCategory(@PathVariable long categoryId, Pageable pageable) throws ProductException {
        return ResponseEntity.ok(productService.findByCategoryId(categoryId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProductsByName(@RequestParam String name, Pageable pageable) throws ProductException {
        return ResponseEntity.ok(productService.findByNameContaining(name, pageable));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) throws ProductException {
        Product createdProduct = productService.createProduct(productRequest);
        return ResponseEntity.ok(createdProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) throws ProductException {
        String response = productService.deleteProduct(productId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product productDetails) throws ProductException {
        Product updatedProduct = productService.updateProduct(productId, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException {
        Product product = productService.findByProductId(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/name/{categoryName}")
    public ResponseEntity<List<Product>> getProductsByCategoryName(@PathVariable String categoryName) throws ProductException {
        List<Product> products = productService.findProductByCategory(categoryName);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/filtered")
    public ResponseEntity<Page<Product>> getFilteredProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,
            Pageable pageable) throws ProductException {
        Page<Product> products = productService.getAllProduct(category, minPrice, maxPrice, minDiscount, sort, stock, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(products);
    }
}



