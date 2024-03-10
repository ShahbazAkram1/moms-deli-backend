package com.momsdeli.online.repository;

import com.momsdeli.online.model.BreadOption;
import com.momsdeli.online.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BreadOptionRepository extends JpaRepository<BreadOption,Long> {

    List<BreadOption> findByCategory(ProductCategory category);
}
