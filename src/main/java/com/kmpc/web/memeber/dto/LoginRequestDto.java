package com.kmpc.web.memeber.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String memberName;
    private String password;
}