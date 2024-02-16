package com.momsdeli.online.repository;

import com.momsdeli.online.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findByName(String name);
//
//    @Query("SELECT c FROM ProductCategory c WHERE c.name = :name AND (c.parentCategory IS NULL OR c.parentCategory.name = :parentCategoryName)")
//    ProductCategory findByNameAndParent(@Param("name") String name, @Param("parentCategoryName") String parentCategoryName);

    @Query("SELECT COUNT(c) > 0 FROM ProductCategory c WHERE LOWER(c.name) = LOWER(:name)")
    boolean existsByName(@Param("name") String name);

//    List<ProductCategory> findByParentCategoryId(Long parentId);
}
