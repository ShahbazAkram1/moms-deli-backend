/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/30/2023$
 * Time: 3:15 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.service.impl;

import com.momsdeli.online.exception.CategoryException;
import com.momsdeli.online.model.ProductCategory;
import com.momsdeli.online.repository.CategoryRepository;
import com.momsdeli.online.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductCategory findById(Long id)  {
        log.info("Finding category by ID:  {}", id);
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public List<ProductCategory> findAll() {
        log.info("Fetching all categories");
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public ProductCategory createCategory(ProductCategory category) throws CategoryException {
        log.info("Creating new category: {}", category.getName());

        // Check if category name already exists
        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryException("A category with the name '" + category.getName() + "' already exists.");
        }


//        // Handling parent category (if applicable)
//        if (category.getParentCategory() != null) {
//            Category parent = categoryRepository.findById(category.getParentCategory().getId())
//                    .orElseThrow(() -> new CategoryException("Parent category not found with ID: " + category.getParentCategory().getId()));
//            category.setParentCategory(parent);
//        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) throws CategoryException {
        log.info("Deleting category with ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new CategoryException("Cannot delete, Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
    @Override
    public ProductCategory updateCategory(Long categoryId, ProductCategory categoryDetails) throws CategoryException {
        System.out.println(categoryDetails);
        log.info("Updating category with ID: {}", categoryId);

        ProductCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found with ID: " + categoryId));

        // Check if the name is being updated to a new value and if it's unique
        if (!category.getName().equals(categoryDetails.getName()) && categoryRepository.existsByName(categoryDetails.getName())) {
            throw new CategoryException("A category with the name '" + categoryDetails.getName() + "' already exists.");
        }

        // Update fields
            category.setName(categoryDetails.getName());
//         // Update parent category (if applicable)
//        if (categoryDetails.getParentCategory() != null) {
//            Category parent = categoryRepository.findById(categoryDetails.getParentCategory().getId())
//                    .orElseThrow(() -> new CategoryException("Parent category not found with ID: " + categoryDetails.getParentCategory().getId()));
//            category.setParentCategory(parent);
//        }

        return categoryRepository.save(category);
    }


    @Override
    @Transactional
    public List<ProductCategory> findByName(String name) {
        log.info("Finding categories by name: {}", name);
        return categoryRepository.findByName(name);
    }

    @Override
    @Transactional
    public List<ProductCategory> findByParentCategoryId(Long parentId) {
        log.info("Finding categories by parent ID: {}", parentId);
//        return categoryRepository.findByParentCategoryId(parentId);
        return null;
    }

    @Override
    @Transactional
    public List<ProductCategory> saveAll(List<ProductCategory> categories) {
        return categoryRepository.saveAll(categories);
    }

    @Override
    @Transactional
    public void deleteAll(List<Long> categoryIds) {
        categoryIds.forEach(categoryRepository::deleteById);
    }

    @Override
    @Transactional
    public void setCategoryStatus(Long categoryId, boolean isActive) {
        ProductCategory category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            // Assuming 'active' is a field in Category. Modify accordingly.
            category.setActive(isActive);
            categoryRepository.save(category);
        }
    }

    @Override
    @Transactional
    public long getTotalCategoryCount() {
        return categoryRepository.count();
    }
}
