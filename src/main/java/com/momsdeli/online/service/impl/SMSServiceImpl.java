package com.momsdeli.online.service.impl;

import com.momsdeli.online.config.TwilioConfig;
import com.momsdeli.online.dto.SMSRequest;
import com.momsdeli.online.service.SMSService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMSServiceImpl implements SMSService {

    private static final Logger logger = LoggerFactory.getLogger(SMSServiceImpl.class);
    private final TwilioConfig twilioConfig;

    @Autowired
    public SMSServiceImpl(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    public void sendSMS(SMSRequest smsRequest) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(smsRequest.getPhoneNumber()),
                    new PhoneNumber(twilioConfig.getPhoneNumber()),
                    smsRequest.getMessage()
            ).create();
            logger.info("Sent SMS with SID: {}", message.getSid());
        } catch (Exception e) {
            logger.error("Failed to send SMS: ", e);
        }
    }
}
