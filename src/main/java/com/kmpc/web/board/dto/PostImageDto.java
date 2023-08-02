package com.kmpc.web.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.kmpc.web.board.entity.PostImage}
 */
@Data
public class PostImageDto implements Serializable {
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private Long id;
    private String imageUrl;
    private String storeFilename;

    @QueryProjection
    @Builder
    public PostImageDto(LocalDateTime createAt, LocalDateTime modifiedAt, Long id, String imageUrl, String storeFilename) {
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.id = id;
        this.imageUrl = imageUrl;
        this.storeFilename = storeFilename;
    }
}