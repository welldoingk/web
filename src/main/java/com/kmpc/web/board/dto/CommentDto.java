package com.kmpc.web.board.dto;

import com.kmpc.web.board.entity.Comment;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.member.entity.Member;
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
    LocalDateTime createAt;
    LocalDateTime modifiedAt;


    public CommentDto(String content) {
        this.content = content;
    }

    public Comment toEntity(Member member, Post post) {
        return Comment.builder()
                .post(post)
                .member(member)
                .content(content)
                .build();
    }
}