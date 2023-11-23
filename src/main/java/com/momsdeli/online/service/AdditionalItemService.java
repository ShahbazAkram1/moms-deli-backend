package com.momsdeli.online.service;

import com.momsdeli.online.model.AdditionalItem;
import com.momsdeli.online.model.Product;
import com.momsdeli.online.model.ProductCategory;

import java.util.List;

public interface AdditionalItemService {
    List<AdditionalItem> getAdditionalItemsForProduct(Product product);
    // Other methods if needed...
    List<AdditionalItem> getAllAdditionalItems();
    AdditionalItem getAdditionalItemById(Long additionalItemId);
    AdditionalItem createAdditionalItem(AdditionalItem additionalItem);
    AdditionalItem updateAdditionalItem(Long additionalItemId, AdditionalItem updatedAdditionalItem);
    boolean deleteAdditionalItem(Long additionalItemId);

    List<AdditionalItem> getAdditionByProductCategory(ProductCategory productCategory);
}
