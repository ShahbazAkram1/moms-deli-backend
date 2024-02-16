package com.momsdeli.online.config;

import com.twilio.http.TwilioRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.twilio.Twilio;

@Configuration
public class TwilioConfig {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String phoneNumber;

    @Bean
    public TwilioRestClient initializeTwilio() {
        Twilio.init(accountSid, authToken);
        return Twilio.getRestClient();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
