package com.momsdeli.online.service;

import com.momsdeli.online.model.BreadOption;
import com.momsdeli.online.model.ProductCategory;

import java.util.List;

public interface BreadOptionService {

    List<BreadOption> getBreadOptionsForProductCategory(ProductCategory productCategory);

    List<BreadOption> getAllBreadOptions();

    BreadOption getBreadOptionById(Long breadOptionId);

    BreadOption createBreadOption(BreadOption breadOption);

    BreadOption updateBreadOption(Long breadOptionId, BreadOption updatedBreadOption);

    boolean deleteBreadOption(Long breadOptionId);
}
