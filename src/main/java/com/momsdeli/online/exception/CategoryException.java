/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/30/2023$
 * Time: 3:17 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.exception;

import org.springframework.http.HttpStatus;

public class CategoryException extends Exception {

    private final HttpStatus httpStatus;

    public CategoryException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CategoryException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // Default status
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
