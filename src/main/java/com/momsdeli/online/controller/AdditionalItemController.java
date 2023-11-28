package com.momsdeli.online.controller;

import com.momsdeli.online.model.AdditionalItem;
import com.momsdeli.online.model.ProductCategory;
import com.momsdeli.online.service.AdditionalItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://momsdelionline.com", "http://localhost:4200"})
@RestController
@RequestMapping("/api/additional-items")
public class AdditionalItemController {
//    http://localhost:8081/api/additional-items/getByProductCategory
    private final AdditionalItemService additionalItemService;

    public AdditionalItemController(AdditionalItemService additionalItemService) {
        this.additionalItemService = additionalItemService;
    }

    @GetMapping
    public ResponseEntity<List<AdditionalItem>> getAllAdditionalItems() {
        List<AdditionalItem> additionalItems = additionalItemService.getAllAdditionalItems();
        return new ResponseEntity<>(additionalItems, HttpStatus.OK);
    }

    @GetMapping("/{additionalItemId}")
    public ResponseEntity<AdditionalItem> getAdditionalItemById(@PathVariable Long additionalItemId) {
        AdditionalItem additionalItem = additionalItemService.getAdditionalItemById(additionalItemId);
        if (additionalItem != null) {
            return new ResponseEntity<>(additionalItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<AdditionalItem> createAdditionalItem(@RequestBody AdditionalItem additionalItem) {
        AdditionalItem createdAdditionalItem = additionalItemService.createAdditionalItem(additionalItem);
        if (createdAdditionalItem != null) {
            return new ResponseEntity<>(createdAdditionalItem, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{additionalItemId}")
    public ResponseEntity<AdditionalItem> updateAdditionalItem(
            @PathVariable Long additionalItemId,
            @RequestBody AdditionalItem updatedAdditionalItem
    ) {
        AdditionalItem updatedItem = additionalItemService.updateAdditionalItem(additionalItemId, updatedAdditionalItem);
        if (updatedItem != null) {
            return new ResponseEntity<>(updatedItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{additionalItemId}")
    public ResponseEntity<Void> deleteAdditionalItem(@PathVariable Long additionalItemId) {
        boolean deleted = additionalItemService.deleteAdditionalItem(additionalItemId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/getByProductCategory")
    public ResponseEntity<List<AdditionalItem>> getAdditionItemsByProductCategory(@RequestBody ProductCategory productCategory){
        List<AdditionalItem> additionByProductCategory = this.additionalItemService.getAdditionByProductCategory(productCategory);
        return   ResponseEntity.ok(additionByProductCategory);
    }
}
