package com.kmpc.web.member.entity;

import com.kmpc.web.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "member")
    private List<Board> boards;

    @ManyToMany
    @JoinTable(
            name = "member_role",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    private Set<Role> role;

}