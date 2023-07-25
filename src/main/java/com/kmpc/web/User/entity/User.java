package com.kmpc.web.User.entity;
import com.kmpc.web.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;            //번호
    @Column(nullable = false, length = 30, unique = true)
    private String username;    //작성자
    private String password;    //비밀번호
    private String nickname;
    private String telNo;     //핸드폰번호
    private String email;     //핸드폰번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; //권한 [ROLE_USER, ROLE_ADMIN]

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();
}