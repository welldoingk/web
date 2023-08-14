package com.kmpc.web.board.dto;

import com.kmpc.web.board.entity.Post;
import com.kmpc.web.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PostDto {

    private Long id; // 시퀀스

    @NotEmpty(message = "제목은 필수입니다.")
    private String title; // 제목

    private String content; // 내용

    private Long boardId; // 조회수

    private String gbVal; // 구분

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;

    private Long viewCount; // 조회수

    private String username; // 사용자 이름

    private String memberId; // 사용자 이름

    private List<MultipartFile> postFiles;

    private Long orders;

    @QueryProjection
    @Builder
    public PostDto(Long id, String title, String content, LocalDateTime createAt, LocalDateTime modifiedAt, Long viewCount,
                   String username, Long boardId, String gbVal, Long orders) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.viewCount = viewCount;
        this.username = username;
        this.boardId = boardId;
        this.gbVal = gbVal;
        this.orders = orders;
    }

    public Post toEntity(Member member) {
        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .boardId(boardId)
                .gbVal(gbVal)
                .orders(orders)
                .build();
    }

}
