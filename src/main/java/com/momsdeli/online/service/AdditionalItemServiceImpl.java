package com.momsdeli.online.service;

import com.momsdeli.online.model.AdditionalItem;
import com.momsdeli.online.model.Product;
import com.momsdeli.online.model.ProductCategory;
import com.momsdeli.online.repository.AdditionalItemRepository;
import com.momsdeli.online.repository.ProductCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdditionalItemServiceImpl implements AdditionalItemService {

    private final AdditionalItemRepository additionalItemRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final Logger logger = LoggerFactory.getLogger(AdditionalItemServiceImpl.class);

    @Autowired
    public AdditionalItemServiceImpl(AdditionalItemRepository additionalItemRepository, ProductCategoryRepository productCategoryRepository) {
        this.additionalItemRepository = additionalItemRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public List<AdditionalItem> getAdditionalItemsForProduct(Product product) {
        logger.info("Retrieving additional items for product: {}", product.getId());
        return additionalItemRepository.findByProducts(product);
    }

    @Override
    public List<AdditionalItem> getAllAdditionalItems() {
        logger.info("Retrieving all additional items.");
        return additionalItemRepository.findAll();
    }

    @Override
    public AdditionalItem getAdditionalItemById(Long additionalItemId) {
        logger.info("Retrieving additional item by ID: {}", additionalItemId);
        Optional<AdditionalItem> optionalAdditionalItem = additionalItemRepository.findById(additionalItemId);
        return optionalAdditionalItem.orElse(null);
    }

    @Override
    public AdditionalItem createAdditionalItem(AdditionalItem additionalItem) {
        logger.info("Creating additional item: {}", additionalItem.getName());
        // You may add validation or checks before saving
        return additionalItemRepository.save(additionalItem);
    }

    @Override
    public AdditionalItem updateAdditionalItem(Long additionalItemId, AdditionalItem updatedAdditionalItem) {
        logger.info("Updating additional item with ID: {}", additionalItemId);
        Optional<AdditionalItem> optionalAdditionalItem = additionalItemRepository.findById(additionalItemId);
        if (optionalAdditionalItem.isPresent()) {
            updatedAdditionalItem.setId(additionalItemId); // Ensure the ID is set for update
            return additionalItemRepository.save(updatedAdditionalItem);
        }
        return null; // Not found, return null or handle differently based on your requirement
    }

    @Override
    public boolean deleteAdditionalItem(Long additionalItemId) {
        logger.info("Deleting additional item with ID: {}", additionalItemId);
        Optional<AdditionalItem> optionalAdditionalItem = additionalItemRepository.findById(additionalItemId);
        if (optionalAdditionalItem.isPresent()) {
            additionalItemRepository.deleteById(additionalItemId);
            return true;
        }
        return false; // Not found or failed to delete
    }

    @Override
    public List<AdditionalItem> getAdditionByProductCategory(ProductCategory productCategory) {
        return  this.additionalItemRepository.findAdditionalItemByCategory(this.productCategoryRepository.findById(productCategory.getId()).orElseThrow(() -> new RuntimeException("Category not found")));
    }
}
