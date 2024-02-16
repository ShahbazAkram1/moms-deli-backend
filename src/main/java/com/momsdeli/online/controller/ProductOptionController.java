package com.momsdeli.online.controller;

import com.momsdeli.online.model.ProductOption;
import com.momsdeli.online.service.impl.ProductOptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"https://momsdelionline.com", "http://localhost:4200"})
//@RequestMapping("/api/")
public class ProductOptionController {

    private final ProductOptionServiceImpl productOptionServiceImpl;

    @Autowired
    public ProductOptionController(ProductOptionServiceImpl productOptionServiceImpl) {
        this.productOptionServiceImpl = productOptionServiceImpl;
    }

    @GetMapping("/product-category/{productCategoryId}/options")
    public ResponseEntity<List<ProductOption>> getProductOptions(@PathVariable Long productCategoryId) {
        List<ProductOption> options = productOptionServiceImpl.getOptionsByProductCategoryId(productCategoryId);
        return new ResponseEntity<>(options, HttpStatus.OK);
    }
}
