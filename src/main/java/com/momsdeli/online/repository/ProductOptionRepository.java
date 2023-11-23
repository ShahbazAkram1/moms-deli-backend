package com.momsdeli.online.repository;

import com.momsdeli.online.model.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    List<ProductOption> findByProductCategoryId(Long productCategoryId);
}
