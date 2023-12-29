/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 12/28/2023$
 * Time: 5:30 PM$
 * Project Name: moms_deli_backend$
 */


package com.momsdeli.online.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginRequest {

    private String email;

    private String password;
}
