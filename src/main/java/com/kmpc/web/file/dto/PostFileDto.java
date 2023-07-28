package com.kmpc.web.file.dto;

import com.kmpc.web.file.entity.File;
import com.kmpc.web.file.entity.PostFile;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;

@Data
public class PostFileDto {

    private Long postFileId;

    private Long id;

    private Long fileId;

    private Long postId;

    private String originFileName;

    private Long size;

    private String extension;

    public PostFileDto(){

    }

    @Builder
    public PostFileDto(Long postId){
        this.postId = postId;
    }

    @QueryProjection
    public PostFileDto(Long postFileId, Long fileId, String originFileName, Long size, String extension){
        this.postFileId = postFileId;
        this.fileId = fileId;
        this.originFileName = originFileName;
        this.size = size;
        this.extension = extension;
    }

    public PostFile toEntity(File file){
        return PostFile.builder()
                .postId(postId)
                .file(file)
                .build();
    }
}