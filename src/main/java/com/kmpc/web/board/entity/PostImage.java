package com.kmpc.web.board.entity;

import com.kmpc.web.util.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PostImage extends Timestamped {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    private String imageUrl;

    private String storeFilename;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostImage(String imageUrl, String storeFilename, Post post) {
        this.imageUrl = imageUrl;
        this.storeFilename = storeFilename;
        this.post = post;
    }

}