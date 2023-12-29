/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/12/2023$
 * Time: 5:53 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.config;

import com.twilio.Twilio;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.accountSid}")
    private String accountSid;
    @Value("${twilio.authToken}")
    private String authToken;
    @Getter
    @Value("${twilio.phoneNumber}")
    private String phoneNumber;

    @Bean
    public void twilioInitializer() {
        Twilio.init(accountSid, authToken);
    }

}