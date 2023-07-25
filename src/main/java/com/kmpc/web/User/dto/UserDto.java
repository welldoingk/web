package com.kmpc.web.User.dto;

import com.kmpc.web.User.entity.Role;
import com.kmpc.web.User.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String username;

    private String password;

    private String nickname;

    private String telNo;

    private String email;

    private Role role;

    /* DTO -> Entity */
    public User toEntity() {
        User user = User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .telNo(telNo)
                .email(email)
                .role(role.USER)
                .build();
        return user;
    }
}