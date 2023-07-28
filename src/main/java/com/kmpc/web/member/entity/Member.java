package com.kmpc.web.member.entity;

import java.util.ArrayList;
import java.util.List;

import com.kmpc.web.board.entity.Post;
import com.kmpc.web.security.UserRoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @Column(nullable = false, unique = true)
    private String memberId;

    @Column(nullable = false)
    private String password;

    private String memberName;

    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    public Member(String memberId, String password, UserRoleEnum role, String memberName, String nickname) {
        this.memberId = memberId;
        this.password = password;
        this.role = role;
        this.memberName = memberName;
        this.nickname = nickname;
    }

}