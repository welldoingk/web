package com.kmpc.web.board.dto;

import com.kmpc.web.board.entity.Comment;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.member.entity.Member;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.kmpc.web.board.entity.Comment}
 */
@Data
public class CommentDto implements Serializable {
    Long id;
    String content;
    LocalDateTime createAt;
    LocalDateTime modifiedAt;

    public CommentDto() {

    }

    public CommentDto(String content) {
        this.content = content;
    }

    public Comment toEntity(Member member, Post post) {
        return Comment.builder()
                .member(member)
                .post(post)
                .content(content)
                .build();
    }
}