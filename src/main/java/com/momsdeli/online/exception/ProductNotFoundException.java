/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 2/3/2024$
 * Time: 3:45 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message){
        super(message);
    }
}
