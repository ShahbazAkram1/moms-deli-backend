package com.momsdeli.online.service.impl;

import com.momsdeli.online.config.TwilioConfig;
import com.momsdeli.online.dto.SMSRequest;
import com.momsdeli.online.dto.SMSResponse;
import com.momsdeli.online.exception.SMSSendException;
import com.momsdeli.online.service.SMSService;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SMSServiceImpl implements SMSService {

    private final TwilioConfig twilioConfig;

    public SMSServiceImpl(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Override
    public SMSResponse sendSMS(SMSRequest request) {
        try {
            String phoneNumber = request.getPhoneNumber();
            String messageText = request.getMessage();

            // Validate phoneNumber and messageText (NotBlank is checked in the request DTO)
            if (phoneNumber != null && messageText != null) {
                Message message = Message.creator(
                        new PhoneNumber(phoneNumber),
                        new PhoneNumber(twilioConfig.getPhoneNumber()),
                        messageText).create();

                if (message != null && message.getSid() != null) {
                    log.info("SMS Sent Successfully to {}: Message SID {}: ", phoneNumber, message.getSid());
                    return new SMSResponse("SMS sent successfully! SID: " + message.getSid());
                } else {
                    log.error("Failed to send SMS. Message or SID is null.");
                    throw new SMSSendException("Failed to send SMS");
                }
            } else {
                log.error("Failed to send SMS. Null values detected in request.");
                throw new SMSSendException("Failed to send SMS. Null values detected in request.");
            }
        } catch (ApiException apiException) {
            log.error("Failed to send SMS to {}: {}", request.getPhoneNumber(), apiException.getMessage());
            throw new SMSSendException("Failed to send SMS", apiException);
        }
    }
}
