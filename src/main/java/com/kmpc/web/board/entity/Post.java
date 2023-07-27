package com.kmpc.web.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kmpc.web.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_seq")
    private Long id; // 번호

    private String bbsNo; // 게시판 번호
    private String title; // 제목
    private String content; // 내용

    @CreatedDate
    private LocalDateTime regDate; // 등록 날짜

    @LastModifiedDate
    private LocalDateTime uptDate; // 수정 날짜

    private Long viewCount; // 조회수
    private String delYn; // 삭제여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq")
    private Member member;

    public Post update(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }

    public Post delete(String delYn) {
        this.delYn = delYn;
        return this;
    }

    @Builder
    public Post(String title, String content, Long viewCount, String delYn, Member member) {
        this.title = title;
        this.content = content;
        this.viewCount = 0L;
        this.delYn = "N";
        this.member = member;
    }
}