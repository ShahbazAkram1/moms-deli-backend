/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/28/2023$
 * Time: 2:46 PM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.service.impl;

import com.momsdeli.online.exception.UserException;
import com.momsdeli.online.model.User;
import com.momsdeli.online.repository.UserRepository;
import com.momsdeli.online.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {


    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if (username == null) {
            throw new UsernameNotFoundException("User not found with email " + username);
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorityList);


    }

    @Override
    public User findUserById(Long userId) throws UserException {
        return null;
    }

    @Override
    public User findUserByJwtToken(String jwt) throws UserException {
        return null;
    }
}
