package com.kmpc.web.member.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.Set;

import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.entity.Role;

@Getter
public class MemberSessionDto implements Serializable {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String telNo;
    private Set<Role> role;

    /* Entity -> Dto */
    public MemberSessionDto(Member user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.email = user.getTelNo();
        this.role = user.getRole();
    }
}