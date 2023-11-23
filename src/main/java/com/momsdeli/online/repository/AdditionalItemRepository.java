package com.momsdeli.online.repository;

import com.momsdeli.online.model.AdditionalItem;
import com.momsdeli.online.model.Product;
import com.momsdeli.online.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdditionalItemRepository extends JpaRepository<AdditionalItem, Long> {
    List<AdditionalItem> findByProducts(Product product);

    List<AdditionalItem> findAdditionalItemByCategory(ProductCategory productCategory);
}


