/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 2/14/2024$
 * Time: 3:15 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
