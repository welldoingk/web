package com.kmpc.web.board.repository;

import com.kmpc.web.board.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}