package com.momsdeli.online.exception;




public class UserNotFoundException  extends RuntimeException{

    public UserNotFoundException(String str){
        super(str);
    }
}
