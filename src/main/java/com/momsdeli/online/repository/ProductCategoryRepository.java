package com.momsdeli.online.repository;

import com.momsdeli.online.model.Product;
import com.momsdeli.online.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

//@CrossOrigin("http://localhost:4200")
@CrossOrigin(origins = {"https://momsdelionline.com", "http://localhost:4200"})
@RepositoryRestResource(collectionResourceRel = "productCategory", path = "product-category")
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
//    Page<Product> findByCategoryId(@RequestParam("id") Long id, Pageable pageable);
}
