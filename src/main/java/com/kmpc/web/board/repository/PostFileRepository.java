package com.kmpc.web.board.repository;

import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    List<PostFile> findByPost(Post post);

}