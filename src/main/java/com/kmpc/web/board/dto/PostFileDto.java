package com.kmpc.web.board.dto;

import com.kmpc.web.board.entity.PostFile;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link PostFile}
 */
@Data
public class PostFileDto implements Serializable {
    private Long id;
    private String fileUrl;
    private String storeFilename;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    @QueryProjection
    @Builder
    public PostFileDto(LocalDateTime createAt, LocalDateTime modifiedAt, Long id, String fileUrl, String storeFilename) {
        this.id = id;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.fileUrl = fileUrl;
        this.storeFilename = storeFilename;
    }
}