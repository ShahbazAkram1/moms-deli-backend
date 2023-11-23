package com.momsdeli.online.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "product_option")
@Data
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "active")
    private boolean active;

    @Column(name = "units_in_stock")
    private int unitsIn_stock;

    @Column(name = "selected")
    private boolean selected;

    @ManyToOne
    @JoinColumn(name = "product_category", nullable = false)
    private ProductCategory productCategory;
}
