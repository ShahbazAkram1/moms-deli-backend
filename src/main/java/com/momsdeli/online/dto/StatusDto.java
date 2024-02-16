package com.momsdeli.online.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDto {
    private Long code;
    private String message;
    private Object data;
}
