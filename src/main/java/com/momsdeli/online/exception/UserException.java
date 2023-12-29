/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/28/2023$
 * Time: 2:32 PM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.exception;

public class UserException extends Exception {

    /**
     * Constructor for UserException.
     *
     * @param message the detail message.
     */
    public UserException(String message) {
        super(message);
    }

    /**
     * Constructor for UserException with cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public UserException(String message, Throwable cause) {

    }


}
