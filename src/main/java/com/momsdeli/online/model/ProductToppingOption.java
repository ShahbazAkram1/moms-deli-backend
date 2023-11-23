package com.momsdeli.online.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_topping_option")
@Data
public class ProductToppingOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

}
