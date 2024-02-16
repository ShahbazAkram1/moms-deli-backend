package com.momsdeli.online.service;

import com.momsdeli.online.exception.UserException;
import com.momsdeli.online.model.User;
import com.momsdeli.online.request.LoginRequest;

public interface UserService {

    User findUserById(Long userId) throws UserException;

    User findUserByJwtToken(String jwt) throws UserException;
    User generateToken(LoginRequest loginRequest);

    User createUser(User user);

}
