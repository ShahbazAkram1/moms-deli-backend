/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 3/6/2024$
 * Time: 2:20 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "bread_options")
@Getter
@Setter
public class BreadOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    private boolean active = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    @ManyToMany(mappedBy = "breadOptions")
    private Set<Product> products = new HashSet<>();

}

