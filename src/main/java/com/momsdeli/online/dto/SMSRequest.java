/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/12/2023$
 * Time: 6:38 AM$
 * Project Name: moms_deli_backend$
 */

package com.momsdeli.online.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class SMSRequest {

    @JsonProperty("phoneNumber")
    @NotBlank
    private String phoneNumber;

    @JsonProperty("message")
    @NotBlank
    private String message;

}
