/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/12/2023$
 * Time: 6:23 AM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.service;

import com.momsdeli.online.dto.SMSRequest;
import com.momsdeli.online.dto.SMSResponse;

public interface SMSService {
    SMSResponse sendSMS(SMSRequest request);
}
