package com.kmpc.web.board.entity;


import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.util.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="commnet_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false)
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    private String delYn;

    public void update(CommentDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

    // 부모 댓글 수정
    public void updateParent(Comment parent) {
        this.parent = parent;
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

//    public CommentDto toDto() {
//        return CommentDto.builder()
//                .id(id)
//                .content(content)
//                .nickname(member.getNickname())
////                .children(children.toListDto())
//                .postId(post.getId())
//                .build();
//    }
//
//


    @Builder
    public Comment(String content,String delYn, Member member, Post post) {
        this.content = content;
        this.delYn = delYn;
        this.member = member;
        this.post = post;
    }

}