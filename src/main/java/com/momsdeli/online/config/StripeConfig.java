/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 1/21/2024$
 * Time: 3:56 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.config;


import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        // Set the Stripe secret key
        Stripe.apiKey = stripeSecretKey;
    }
}