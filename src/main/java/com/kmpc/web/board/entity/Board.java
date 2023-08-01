package com.kmpc.web.board.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kmpc.web.util.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Board extends Timestamped {

    @Id
    private Long boardId; // 번호

    private String boardName;


    private String delYn; // 삭제여부

    public Board delete(String delYn) {
        this.delYn = delYn;
        return this;
    }

    @Builder
    public Board(String boardName, Long boardId) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.delYn = "N";
    }
}