package com.momsdeli.online.controller;

import com.momsdeli.online.model.ProductOption;
import com.momsdeli.online.service.ProductOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"https://momsdelionline.com", "http://localhost:4200"})
//@RequestMapping("/api/")
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    @Autowired
    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    @GetMapping("/product-category/{productCategoryId}/options")
    public ResponseEntity<List<ProductOption>> getProductOptions(@PathVariable Long productCategoryId) {
        List<ProductOption> options = productOptionService.getOptionsByProductCategoryId(productCategoryId);
        return new ResponseEntity<>(options, HttpStatus.OK);
    }
}
