package com.kmpc.web.Member.dto;

import com.kmpc.web.Member.entity.Member;
import com.kmpc.web.Member.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private String username;
    private String password;
    private String nickname;
    private String telNo;
    private String email;
    private Role role;

    /* DTO -> Entity */
    public Member toEntity() {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .telNo(telNo)
                .email(email)
                .role(role.ROLE_USER)
                .build();
        return member;
    }
}