/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 2/21/2024$
 * Time: 7:33 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.service.impl;

import com.momsdeli.online.service.EmailService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public String loadEmailTemplate() throws IOException {
        InputStreamSource resource = new ClassPathResource("templates/order_confirmation_email.html");
        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
