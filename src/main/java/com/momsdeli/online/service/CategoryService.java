package com.momsdeli.online.service;


import com.momsdeli.online.exception.CategoryException;
import com.momsdeli.online.model.ProductCategory;

import java.util.List;

public interface CategoryService {

    ProductCategory findById(Long id);

    List<ProductCategory> findAll();

    ProductCategory createCategory(ProductCategory category) throws CategoryException;

    void deleteById(Long id) throws CategoryException;

    ProductCategory updateCategory(Long categoryId, ProductCategory category) throws CategoryException;

    List<ProductCategory> findByName(String name);

    List<ProductCategory> findByParentCategoryId(Long parentId);

    List<ProductCategory> saveAll(List<ProductCategory> categories);

    void deleteAll(List<Long> categoryIds);

    void setCategoryStatus(Long categoryId, boolean isActive);

    long getTotalCategoryCount();

}
