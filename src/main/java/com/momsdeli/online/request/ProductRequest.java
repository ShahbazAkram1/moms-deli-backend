/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/29/2023$
 * Time: 5:56 PM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.request;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductRequest {


    private Long categoryId; // To link product with a category
    private String name;
    private String description;
    private BigDecimal price; // Using BigDecimal for monetary values
    private String imageUrl;
    private boolean active;
    private int unitsInStock;
    private BigDecimal discountedPrice; // Assuming you want to accept a discounted price
    private Integer discountedPercent; // Assuming this is a percentage of discount

}
