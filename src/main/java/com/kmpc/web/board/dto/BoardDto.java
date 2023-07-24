package com.kmpc.web.board.dto;

import com.kmpc.web.User.entity.User;
import com.kmpc.web.board.entity.Board;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * packageName    : jpa.board.dto
 * fileName       : BoardDto
 * author         : 김재성
 * date           : 2022-08-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-08-02        김재성       최초 생성
 */

@Data
public class BoardDto {

    private Long id;            //시퀀스

    @NotEmpty(message = "제목은 필수입니다.")
    private String title;              //제목
    private String content;            //내용
    private LocalDateTime regDate;     //등록 날짜
    private LocalDateTime uptDate;     //수정 날짜
    private Long viewCount;            //조회수
    private String username;            //사용자 이름

    public BoardDto() {

    }

    public BoardDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @QueryProjection
    public BoardDto(Long id, String title, String content, LocalDateTime regDate, LocalDateTime uptDate, Long viewCount, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.regDate = regDate;
        this.uptDate = uptDate;
        this.viewCount = viewCount;
        this.username = username;
    }

    public Board toEntity(User user) {
        return Board.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
    }
}
