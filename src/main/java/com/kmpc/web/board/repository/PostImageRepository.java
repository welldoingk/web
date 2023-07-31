package com.kmpc.web.board.repository;

import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findByPost(Post post);

}