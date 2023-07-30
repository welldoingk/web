package com.kmpc.web.board.entity;

import com.kmpc.web.util.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id; // 번호

    private String boardName; // 게시판 번호
    
    private String boardTpye; // 게시판 타입
    
    private String delYn; // 삭제여부

    public Board delete(String delYn) {
        this.delYn = delYn;
        return this;
    }

    @Builder
    public Board(String boardName) {
        this.boardName = boardName;
        this.delYn = "N";
    }
}