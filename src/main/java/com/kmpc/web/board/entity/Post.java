package com.kmpc.web.board.entity;

import com.kmpc.web.board.dto.MtPostDto;
import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.util.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id; // 번호
    private Long boardId; // 게시판 번호
    private String title; // 제목
    private String content; // 내용
    private String gbVal; // 구분값
    @ColumnDefault("0")
    private Long viewCount; // 조회수
    private String delYn; // 삭제여부

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostImage> postImages;

    public Post update(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }

    public Post delete(String delYn) {
        this.delYn = delYn;
        return this;
    }

    public Post updateViewCount(Long viewCount) {
        this.viewCount = viewCount + 1;
        return this;
    }

    public PostDto toDto() {
        return PostDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .viewCount(viewCount)
                .boardId(boardId)
                .gbVal(gbVal)
                .username(member.getMemberName())
                .build();
    }
    public MtPostDto toMtDto() {
        return MtPostDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .viewCount(viewCount)
                .boardId(boardId)
                .gbVal(gbVal)
                .username(member.getMemberName())
                .build();
    }

    @Builder
    public Post(String title, String content, Long viewCount, String delYn, Member member, Long boardId, String gbVal) {
        this.title = title;
        this.content = content;
        this.viewCount = 0L;
        this.delYn = "N";
        this.member = member;
        this.boardId = boardId;
        this.gbVal = gbVal;
    }
}