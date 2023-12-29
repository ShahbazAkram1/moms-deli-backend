package com.momsdeli.online.service;

import com.momsdeli.online.exception.UserException;
import com.momsdeli.online.model.User;

public interface UserService {

    User findUserById(Long userId) throws UserException;

    User findUserByJwtToken(String jwt) throws UserException;


}
