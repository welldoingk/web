package com.kmpc.web.board.dto;

import com.kmpc.web.board.entity.Comment;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.member.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link com.kmpc.web.board.entity.Comment}
 */
@Data
@NoArgsConstructor
public class CommentDto {
    Long id;
    String content;
    Long postId;
    String memberId;
    String nickname;
    String delYn;
    List<CommentDto> children = new ArrayList<>();
    LocalDateTime createAt;
    LocalDateTime modifiedAt;


    public CommentDto(String content) {
        this.content = content;
    }

    @Builder
    public CommentDto(Long id, String content, Long postId, String memberId, String nickname, List<CommentDto> children, LocalDateTime createAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.children = children;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
    }

    public CommentDto(Long id, String content, String memberId, String nickname, LocalDateTime createAt, String delYn) {
        this.id = id;
        this.content = content;
        this.memberId = memberId;
        this.nickname = nickname;
        this.createAt = createAt;
        this.delYn = delYn;
    }

    public static CommentDto convertCommentToDto(Comment comment) {
        String  nick = comment.getMember().getMemberName() +'/'+ comment.getMember().getLocal()+'/'+comment.getMember().getBirthYear().substring(2);
        return new CommentDto(comment.getId(), comment.getContent(), comment.getMember().getMemberId(), nick, comment.getCreateAt(), comment.getDelYn());
    }

    public Comment toEntity(Member member, Post post) {
        return Comment.builder()
                .post(post)
                .member(member)
                .content(content)
                .build();
    }
}