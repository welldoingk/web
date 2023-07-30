package com.kmpc.web.board.dto;

import com.kmpc.web.board.entity.Board;
import com.kmpc.web.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BoardDto {

    private Long id;            //시퀀스

    private String boardName;              //제목

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;     //수정 날짜

    public BoardDto(){

    }

    public BoardDto(String boardName){
        this.boardName = boardName;
    }

    @QueryProjection
    public BoardDto(Long id, String boardName, LocalDateTime createAt , LocalDateTime modifiedAt, Long viewCount){
        this.id = id;
        this.boardName = boardName;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;

    }

    public Board toEntity(){
        return Board.builder()
                .boardName(boardName)
                .build();
    }
}
