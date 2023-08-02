package com.kmpc.web.board.dto;

import com.kmpc.web.board.entity.Comment;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.member.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.kmpc.web.board.entity.Comment}
 */
@Data
@NoArgsConstructor
public class CommentDto {
    Long id;
    String content;
    Long postId;
    Long parentId;
    String memberName;
    LocalDateTime createAt;
    LocalDateTime modifiedAt;


    public CommentDto(String content) {
        this.content = content;
    }

    @Builder
    public CommentDto(Long id, String content, Long postId, Long parentId, String memberName, LocalDateTime createAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.parentId = parentId;
        this.memberName = memberName;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
    }


    public Comment toEntity(Member member, Post post) {
        return Comment.builder()
                .post(post)
                .member(member)
                .content(content)
                .build();
    }
}