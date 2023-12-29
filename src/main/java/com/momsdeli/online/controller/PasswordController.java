/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/29/2023$
 * Time: 2:03 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.controller;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/hash")
    public String hashPassword(@RequestParam(value = "password") String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}

