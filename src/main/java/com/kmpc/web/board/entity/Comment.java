package com.kmpc.web.board.entity;


import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.util.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false)
    private String content;

    private String delYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

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

    @Builder
    public Comment(String content,String delYn, Member member, Post post) {
        this.content = content;
        this.delYn = delYn;
        this.member = member;
        this.post = post;
    }

}