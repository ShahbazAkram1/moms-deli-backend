/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/30/2023$
 * Time: 4:11 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.controller;


import com.momsdeli.online.dto.StatusDto;
import com.momsdeli.online.exception.CategoryException;
import com.momsdeli.online.model.ProductCategory;
import com.momsdeli.online.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Validated
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getCategoryById(@PathVariable Long id) throws CategoryException {
        ProductCategory category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllCategories() {
        List<ProductCategory> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductCategory> createCategory(@RequestBody @Valid ProductCategory category) throws CategoryException {
        ProductCategory createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductCategory> updateCategory(@PathVariable Long id, @RequestBody @Valid ProductCategory categoryDetails)
            throws CategoryException {
        ProductCategory updatedCategory = categoryService.updateCategory(id, categoryDetails);
        return ResponseEntity.ok(updatedCategory);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusDto> deleteCategory(@PathVariable Long id) throws CategoryException {
        try {

            categoryService.deleteById(id);
            StatusDto statusDto = new StatusDto();
            statusDto.setCode(200L);
            statusDto.setMessage("Data Deleted Successfully!");
            return ResponseEntity.ok(statusDto);
        }catch (Exception ee ){
            StatusDto statusDto = new StatusDto();
            statusDto.setCode(400L);
            statusDto.setMessage("Could not be deleted...");
            return ResponseEntity.ok(statusDto);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductCategory>> getCategoriesByName(@PathVariable String name) {
        List<ProductCategory> categories = categoryService.findByName(name);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<ProductCategory>> getCategoriesByParentId(@PathVariable Long parentId) {
        List<ProductCategory> categories = categoryService.findByParentCategoryId(parentId);
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProductCategory>> createCategories(@RequestBody List<ProductCategory> categories) {
        List<ProductCategory> createdCategories = categoryService.saveAll(categories);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategories);
    }

    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteCategories(@RequestBody List<Long> categoryIds) {
        categoryService.deleteAll(categoryIds);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status/{isActive}")
    public ResponseEntity<Void> setCategoryStatus(@PathVariable Long id, @PathVariable boolean isActive) {
        categoryService.setCategoryStatus(id, isActive);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCategoryCount() {
        long categoryCount = categoryService.getTotalCategoryCount();
        return ResponseEntity.ok(categoryCount);
    }

}
