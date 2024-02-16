package com.momsdeli.online.dto;


import com.momsdeli.online.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JwtDtoResponse {
    private String message;
    private String token;
    private User user;
}
