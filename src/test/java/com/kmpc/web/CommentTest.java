package com.kmpc.web;

import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.board.entity.Comment;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.repository.CommentRepository;
import com.kmpc.web.board.repository.Impl.CommentCustomRepositoryImpl;
import com.kmpc.web.board.repository.PostCustomRepository;
import com.kmpc.web.board.service.CommentService;
import com.kmpc.web.board.service.PostService;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommentTest {


    @Autowired
    CommentCustomRepositoryImpl commentCustomRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    PostService postService;

    @Test
    public void test() {
//        // given
//        Member member = memberRepository.findByMemberId("1").get();
        Post post = postService.selectPostDetail(1L);
//
//        CommentDto content = CommentDto.builder()
//                                .content("content")
//                                .postId(post.getId())
//                                .build();
//        // when
//        CommentDto dto = commentService.save(content);
//
//        // then
//        Comment result = commentRepository.findById(dto.getId()).get();
//        assertThat(result.getId()).isEqualTo(dto.getId());
//        assertThat(result.getContent()).isEqualTo(dto.getContent());
//        assertThat(result.getParent()).isNull();
//        assertThat(result.getChildren().size()).isEqualTo(0);
        List<CommentDto> commentsByPostId = commentService.findCommentsByPostId(post.getId());

        System.out.println(commentsByPostId);

    }

}
