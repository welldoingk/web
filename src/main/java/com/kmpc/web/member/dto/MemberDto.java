package com.kmpc.web.member.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.entity.Role;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String nickname;
    private String telNo;
    private String email;
    private Set<Role> role;

    /* DTO -> Entity */
    public Member toEntity() {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .telNo(telNo)
                .email(email)
                .role(role)
                .build();
        return member;
    }
}