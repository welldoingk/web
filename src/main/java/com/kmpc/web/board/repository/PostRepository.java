package com.kmpc.web.board.repository;

import com.kmpc.web.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM post "
            + "WHERE post_id = " +
            "(SELECT prev_no FROM (SELECT post_id, LAG(post_id, 1, -1) OVER(ORDER BY post_id) AS prev_no "
            + "FROM post WHERE 1=1 AND del_yn != 'Y' AND board_id = '3') B "
            + "WHERE post_id = :id )", nativeQuery = true)
    Post findRecentPrevPost(@Param("id") Long id);

    @Query(value = "SELECT * FROM post "
			+ "WHERE post_id = (SELECT prev_no FROM (SELECT post_id, LEAD(post_id, 1, -1) OVER(ORDER BY post_id) AS prev_no "
            + "FROM post WHERE 1=1 AND del_yn != 'Y' AND board_id = '3') B "
            + "WHERE post_id = :id )", nativeQuery = true)
    Post findRecentNextPost(@Param("id") Long id);

    @Query(value = "SELECT * FROM post "
            + "WHERE post_id = "
             + "(SELECT prev_no FROM (SELECT post_id, LAG(post_id, 1, -1) OVER(ORDER BY post_id) AS prev_no "
            + "FROM post WHERE 1=1 AND del_yn != 'Y' AND board_id = '3' AND GB_VAL = :gbVal) B "
            + "WHERE post_id = :id )", nativeQuery = true)
    Post findMtPrevPost(@Param("id") Long postId,@Param("gbVal") String gbVal);

    @Query(value = "SELECT * FROM post "
			+ "WHERE post_id = (SELECT prev_no FROM (SELECT post_id, LEAD(post_id, 1, -1) OVER(ORDER BY post_id) AS prev_no "
            + "FROM post WHERE 1=1 AND del_yn != 'Y' AND board_id = '3' AND GB_VAL = :gbVal) B "
            + "WHERE post_id = :id )", nativeQuery = true)
    Post findMtNextPost(@Param("id") Long postId,@Param("gbVal") String gbVal);

    @Query(value = "SELECT * FROM post "
            + "WHERE post_id = "
             + "(SELECT prev_no FROM (SELECT post_id, LAG(post_id, 1, -1) OVER(ORDER BY post_id) AS prev_no "
            + "FROM post WHERE 1=1 AND del_yn != 'Y' AND board_id = '4' AND GB_VAL = :gbVal) B "
            + "WHERE post_id = :id )", nativeQuery = true)
    Post findEventPrevPost(@Param("id") Long postId,@Param("gbVal") String gbVal);

    @Query(value = "SELECT * FROM post "
			+ "WHERE post_id = (SELECT prev_no FROM (SELECT post_id, LEAD(post_id, 1, -1) OVER(ORDER BY post_id) AS prev_no "
            + "FROM post WHERE 1=1 AND del_yn != 'Y' AND board_id = '4' AND GB_VAL = :gbVal) B "
            + "WHERE post_id = :id )", nativeQuery = true)
    Post findEventNextPost(@Param("id") Long postId,@Param("gbVal") String gbVal);

    @Query(value = "SELECT * FROM post "
            + "WHERE post_id = " +
            "(SELECT prev_no FROM (SELECT post_id, LAG(post_id, 1, -1) OVER(ORDER BY post_id) AS prev_no "
            + "FROM post WHERE 1=1 AND del_yn != 'Y' AND board_id = '3' AND MEMBER_ID = :memberId) B "
            + "WHERE post_id = :id )", nativeQuery = true)
    Post findMemberPrevPost(@Param("id") Long postId,@Param("memberId") String memberId);


    @Query(value = "SELECT * FROM post "
			+ "WHERE post_id = (SELECT prev_no FROM (SELECT post_id, LEAD(post_id, 1, -1) OVER(ORDER BY post_id) AS prev_no "
            + "FROM post WHERE 1=1 AND del_yn != 'Y' AND board_id = '3' AND MEMBER_ID = :memberId) B "
            + "WHERE post_id = :id )", nativeQuery = true)
    Post findMemberNextPost(@Param("id") Long postId,@Param("memberId") String memberId);
}
