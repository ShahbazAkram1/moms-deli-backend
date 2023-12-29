package com.momsdeli.online.repository;

import com.momsdeli.online.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    Category findByName(String name);

}
