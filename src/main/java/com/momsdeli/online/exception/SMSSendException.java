/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/12/2023$
 * Time: 7:01 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.exception;

public class SMSSendException extends RuntimeException{

    public SMSSendException(String message){
        super(message);
    }

    public SMSSendException(String message,Throwable throwable){

    }
}