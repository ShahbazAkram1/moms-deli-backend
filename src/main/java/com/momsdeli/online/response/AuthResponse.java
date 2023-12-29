/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/28/2023$
 * Time: 5:04 PM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String jwt;
    private String message;
}
