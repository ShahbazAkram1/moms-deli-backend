/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/28/2023$
 * Time: 2:46 PM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.service.impl;

import com.momsdeli.online.dto.JwtDtoResponse;
import com.momsdeli.online.exception.UserException;
import com.momsdeli.online.exception.UserNotFoundException;
import com.momsdeli.online.model.User;
import com.momsdeli.online.repository.UserRepository;
import com.momsdeli.online.request.LoginRequest;
import com.momsdeli.online.service.UserService;
import com.momsdeli.online.utils.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private JwtHelper tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = userRepository.findByEmail(username);
//
//        if (username == null) {
//            throw new UsernameNotFoundException("User not found with email " + username);
//        }
//        List<GrantedAuthority> authorityList = new ArrayList<>();
//
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorityList);
//
//
//    }

    @Override
    public User findUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            String errorMessage = String.format("User with ID %d not found", userId);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
    }

    @Override
    public User findUserByJwtToken(String jwt) throws UserException {
        return null;
    }

    @Override
    public User generateToken(LoginRequest loginDto) {
        System.out.println(loginDto);

        User userDetails = userRepository.findByEmail(loginDto.getEmail());

        if (userDetails != null) {
            if (passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
                try {
                    String token = tokenProvider.generateToken(userDetails);
                    userDetails.setToken(token);
                    return userDetails;
                } catch (Exception e) {
                    // Log or print the exception details
                    e.printStackTrace();
                    throw new RuntimeException("Error generating token");
                }
            } else {
                throw new UserNotFoundException("Invalid username or password");
            }
        } else {
            throw new UserNotFoundException("User Not Found");
        }
    }

    @Override
    public User createUser(User user) {
        return this.userRepository.save(user);
    }
}
