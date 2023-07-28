package com.kmpc.web.board.dto;

import com.kmpc.web.board.entity.Post;
import com.kmpc.web.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto {

    private Long id; // 시퀀스

    @NotEmpty(message = "제목은 필수입니다.")
    private String title; // 제목

    private String content; // 내용

    private LocalDateTime regDate; // 등록 날짜

    private LocalDateTime uptDate; // 수정 날짜

    private Long viewCount; // 조회수

    private String username; // 사용자 이름

    private List<MultipartFile> multipartFile;

    public PostDto() {

    }

    public PostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @QueryProjection
    public PostDto(Long id, String title, String content, LocalDateTime regDate, LocalDateTime uptDate, Long viewCount,
            String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.regDate = regDate;
        this.uptDate = uptDate;
        this.viewCount = viewCount;
        this.username = username;
    }

    public Post toEntity(Member member) {
        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .build();
    }
}
