package com.kmpc.web.Member.dto;

import lombok.Getter;

import java.io.Serializable;

import com.kmpc.web.Member.entity.Member;
import com.kmpc.web.Member.entity.Role;

@Getter
public class MemberSessionDto implements Serializable {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private Role role;

    /* Entity -> Dto */
    public MemberSessionDto(Member user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}