package com.kmpc.web.board.entity;

import com.kmpc.web.util.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PostFile extends Timestamped {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_file_id")
    private Long id;

    private String fileUrl;

    private String storeFilename;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostFile(String fileUrl, String storeFilename, Post post) {
        this.fileUrl = fileUrl;
        this.storeFilename = storeFilename;
        this.post = post;
    }

}