package com.kmpc.web.board.repository;

import com.kmpc.web.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM post "
            + "WHERE post_id = " +
            "(SELECT prev_no FROM (SELECT post_id, LAG(post_id, 1, -1) OVER(ORDER BY post_id) AS prev_no FROM post) B "
            + "WHERE post_id = :id AND del_yn != 'Y' AND board_id = '3')", nativeQuery = true)
    Post findPrevPost(@Param("id") Long id);

    @Query(value = "SELECT * FROM post "
			+ "WHERE post_id = (SELECT prev_no FROM (SELECT post_id, LEAD(post_id, 1, -1) OVER(ORDER BY post_id) AS prev_no FROM post) B "
            + "WHERE post_id = :id AND del_yn != 'Y' AND board_id = '3')", nativeQuery = true)
    Post findNextPost(@Param("id") Long id);

}
