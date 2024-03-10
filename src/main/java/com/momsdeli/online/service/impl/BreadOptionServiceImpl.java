/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 3/6/2024$
 * Time: 2:33 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.service.impl;

import com.momsdeli.online.model.BreadOption;
import com.momsdeli.online.model.ProductCategory;
import com.momsdeli.online.repository.BreadOptionRepository;
import com.momsdeli.online.service.BreadOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class BreadOptionServiceImpl implements BreadOptionService {

    private static final Logger logger = LoggerFactory.getLogger(BreadOptionServiceImpl.class);

    private final BreadOptionRepository breadOptionRepository;

    @Autowired
    public BreadOptionServiceImpl(BreadOptionRepository breadOptionRepository) {
        this.breadOptionRepository = breadOptionRepository;
    }

    @Override
    public List<BreadOption> getBreadOptionsForProductCategory(ProductCategory productCategory) {
        logger.info("Retrieving bread options for product category: {}", productCategory.getName());
        return breadOptionRepository.findByCategory(productCategory);
    }

    @Override
    public List<BreadOption> getAllBreadOptions() {
        logger.info("Retrieving all bread options.");

        long startTime = System.nanoTime(); // Start the timer

        List<BreadOption> breadOptions = breadOptionRepository.findAll();

        long endTime = System.nanoTime(); // Stop the timer
        long durationInMilliseconds = (endTime - startTime) / 1000000; // Convert to milliseconds

        logger.info("Retrieved all bread options in {} milliseconds.", durationInMilliseconds);

        return breadOptions;
    }

    @Override
    public BreadOption getBreadOptionById(Long breadOptionId) {
        logger.info("Retrieving bread option by ID: {}", breadOptionId);
        Optional<BreadOption> optionalBreadOption = breadOptionRepository.findById(breadOptionId);
        return optionalBreadOption.orElse(null);
    }

    @Override
    public BreadOption createBreadOption(@NotNull @Valid BreadOption breadOption) {
        logger.info("Creating bread option: {}", breadOption.getName());
        return breadOptionRepository.save(breadOption);
    }

    @Override
    public BreadOption updateBreadOption(Long breadOptionId, @NotNull @Valid BreadOption updatedBreadOption) {
        logger.info("Updating bread option with ID: {}", breadOptionId);
        Optional<BreadOption> optionalBreadOption = breadOptionRepository.findById(breadOptionId);
        if (optionalBreadOption.isPresent()) {
            updatedBreadOption.setId(breadOptionId);
            return breadOptionRepository.save(updatedBreadOption);
        } else {
            logger.error("Bread option with ID {} not found for update.", breadOptionId);
            return null;
        }
    }

    @Override
    public boolean deleteBreadOption(Long breadOptionId) {
        logger.info("Deleting bread option with ID: {}", breadOptionId);
        Optional<BreadOption> optionalBreadOption = breadOptionRepository.findById(breadOptionId);
        if (optionalBreadOption.isPresent()) {
            breadOptionRepository.deleteById(breadOptionId);
            return true;
        } else {
            logger.error("Bread option with ID {} not found for deletion.", breadOptionId);
            return false;
        }
    }
}
