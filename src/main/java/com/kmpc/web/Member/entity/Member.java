package com.kmpc.web.Member.entity;

import com.kmpc.web.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; // 번호
    @Column(nullable = false, length = 30, unique = true)
    private String username; // 작성자
    private String password; // 비밀번호
    private String nickname;
    private String telNo; // 핸드폰번호
    private String email; // 핸드폰번호

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Role role; // 권한 [ROLE_USER, ROLE_ADMIN]

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @Builder
    public Member(String username,
            String password,
            String nickname,
            String telNo,
            String email,
            Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.telNo = telNo;
        this.email = email;
        this.role = role;
    }
}