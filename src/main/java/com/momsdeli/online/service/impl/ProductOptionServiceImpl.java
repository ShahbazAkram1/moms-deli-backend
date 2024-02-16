package com.momsdeli.online.service.impl;

import com.momsdeli.online.model.ProductOption;
import com.momsdeli.online.repository.ProductOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductOptionServiceImpl {

    private final ProductOptionRepository productOptionRepository;

    @Autowired
    public ProductOptionServiceImpl(ProductOptionRepository productOptionRepository) {
        this.productOptionRepository = productOptionRepository;
    }

    public List<ProductOption> getOptionsByProductCategoryId(Long productCategoryId) {
        return productOptionRepository.findByProductCategoryId(productCategoryId);
    }
}
