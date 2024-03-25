package com.momsdeli.online.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="order_item")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="unit_price")
    private BigDecimal unitPrice;

    @Column(name="quantity")
    private int quantity;

    @Column(name="product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable=false, updatable=false)
    private Product product;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_item_additional_item",
            joinColumns = @JoinColumn(name = "order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "additional_item_id")
    )
    private Set<AdditionalItem> selectedAdditionalItems = new HashSet<>();
//    private Set<BreadOption> selectedBread = new HashSet<>();
//    hitte poye bread jo b ando  rakho na po ada
}