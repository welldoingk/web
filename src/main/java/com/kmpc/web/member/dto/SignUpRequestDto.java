package com.kmpc.web.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String memberId;
    private String password;
    private String role;
    private String memberName;
    private String nickname;
    private String local;
    private String birthYear;
}