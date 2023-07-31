package com.kmpc.web.member.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kmpc.web.board.entity.Post;
import com.kmpc.web.security.UserRoleEnum;
import com.kmpc.web.util.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
public class Member extends Timestamped {

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

    @Builder
    public Member(String memberId, String password, String memberName, String nickname) {
        this.memberId = memberId;
        this.password = password;
        this.role = UserRoleEnum.MEMBER;
        this.memberName = memberName;
        this.nickname = nickname;
    }

}