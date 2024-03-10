/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 3/6/2024$
 * Time: 2:35 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.momsdeli.online.model.BreadOption;
import com.momsdeli.online.model.ProductCategory;
import com.momsdeli.online.service.BreadOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/bread-options")
public class BreadOptionController {

    private static final Logger logger = LoggerFactory.getLogger(BreadOptionController.class);

    private final BreadOptionService breadOptionService;

    public BreadOptionController(BreadOptionService breadOptionService) {
        this.breadOptionService = breadOptionService;
    }

    @PostMapping("/getByProductCategory")
    public ResponseEntity<List<BreadOption>> getBreadOptionsByProductCategory(@RequestBody @NotNull ProductCategory productCategory) {
        logger.info("Fetching bread options for product category: {}", productCategory.getName());
        List<BreadOption> breadOptions = breadOptionService.getBreadOptionsForProductCategory(productCategory);
        return ResponseEntity.ok(breadOptions);
    }

    @GetMapping
    public ResponseEntity<List<BreadOption>> getAllBreadOptions() {
        logger.info("Fetching all bread options.");
        List<BreadOption> breadOptions = breadOptionService.getAllBreadOptions();
        return ResponseEntity.ok(breadOptions);
    }

    @GetMapping("/{breadOptionId}")
    public ResponseEntity<BreadOption> getBreadOptionById(@PathVariable Long breadOptionId) {
        logger.info("Fetching bread option by ID: {}", breadOptionId);
        BreadOption breadOption = breadOptionService.getBreadOptionById(breadOptionId);
        if (breadOption != null) {
            return ResponseEntity.ok(breadOption);
        } else {
            logger.error("Bread option with ID {} not found.", breadOptionId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BreadOption> createBreadOption(@RequestBody @Valid BreadOption breadOption) {
        logger.info("Creating new bread option: {}", breadOption.getName());
        BreadOption createdBreadOption = breadOptionService.createBreadOption(breadOption);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBreadOption);
    }

    @PutMapping("/{breadOptionId}")
    public ResponseEntity<BreadOption> updateBreadOption(
            @PathVariable Long breadOptionId,
            @RequestBody @Valid BreadOption updatedBreadOption
    ) {
        logger.info("Updating bread option with ID: {}", breadOptionId);
        BreadOption updatedOption = breadOptionService.updateBreadOption(breadOptionId, updatedBreadOption);
        if (updatedOption != null) {
            return ResponseEntity.ok(updatedOption);
        } else {
            logger.error("Bread option with ID {} not found for update.", breadOptionId);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{breadOptionId}")
    public ResponseEntity<Void> deleteBreadOption(@PathVariable Long breadOptionId) {
        logger.info("Deleting bread option with ID: {}", breadOptionId);
        boolean deleted = breadOptionService.deleteBreadOption(breadOptionId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            logger.error("Bread option with ID {} not found for deletion.", breadOptionId);
            return ResponseEntity.notFound().build();
        }
    }
}
