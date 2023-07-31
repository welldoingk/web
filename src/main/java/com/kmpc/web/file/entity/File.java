package com.kmpc.web.file.entity;

import com.kmpc.web.util.Timestamped;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class File extends Timestamped  {

    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private Long id; // id

    @Column(nullable = false)
    private String originFileName; // 원본 파일명

    @Column(nullable = false)
    private String savedFileName; // 저장된 파일명

    private String uploadDir; // 경로명

    private String extension; // 확장자

    private Long size; // 파일 사이즈

    private String contentType; // ContentType


    @OneToOne(mappedBy = "file")
    private PostFile postFile;

    @Builder
    public File(Long id, String originFileName, String savedFileName, String uploadDir, String extension, Long size,
            String contentType) {
        this.id = id;
        this.originFileName = originFileName;
        this.savedFileName = savedFileName;
        this.uploadDir = uploadDir;
        this.extension = extension;
        this.size = size;
        this.contentType = contentType;
    }
}