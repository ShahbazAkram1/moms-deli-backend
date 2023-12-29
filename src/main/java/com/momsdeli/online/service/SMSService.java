package com.momsdeli.online.service;

import com.momsdeli.online.dto.SMSRequest;

public interface SMSService {

    public void sendSMS(SMSRequest smsRequest);
}
