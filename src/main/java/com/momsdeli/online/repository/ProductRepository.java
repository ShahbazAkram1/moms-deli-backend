package com.momsdeli.online.repository;

import com.momsdeli.online.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;


//@CrossOrigin("http://localhost:4200")
//@CrossOrigin(origins = {"https://momsdelionline.com", "http://localhost:4200"})
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long id, Pageable pageable);
//    Page<Product> findByNameContaining(@RequestParam("name") String name, Pageable pageable);

    Page<Product> findByCategoryName(String categoryName, Pageable pageable);

}
