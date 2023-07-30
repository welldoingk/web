package com.kmpc.web.file.entity;

import com.kmpc.web.util.Timestamped;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class PostFile extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_file_id")
    private Long id;            //번호

    private Long postId;
    private String delYn;

    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Builder
    public PostFile(Long postId, Long fileId, String delYn, File file){
        this.postId = postId;
        this.delYn = "N";
        this.file = file;
    }

    public PostFile delete(String delYn){
        this.delYn = delYn;
        return this;
    }
}