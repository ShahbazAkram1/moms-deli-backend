/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/29/2023$
 * Time: 5:56 PM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductRequest {

    private String title;
    private String description;
    private Integer discountedPrice;
    private Integer discountedPresent;
    private Integer quantity;
    private String imageUrl;


}
