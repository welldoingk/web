package com.kmpc.web.board.service;

import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.board.dto.CommentRequestDto;
import com.kmpc.web.board.entity.Comment;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.repository.CommentCustomRepository;
import com.kmpc.web.board.repository.CommentRepository;
import com.kmpc.web.board.repository.PostRepository;
import com.kmpc.web.member.repository.MemberRepository;
import com.kmpc.web.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommonUtil commonUtil;
    private final MemberRepository memberRepository;
    private final CommentCustomRepository commentCustomRepository;

    public List<CommentDto> findCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId).get();
        return commentCustomRepository.getAllCommentsByPost(post);
    }

    @Transactional
    public CommentDto save(CommentRequestDto requestDto) {


        Post post = postRepository.findById(requestDto.getPostId()).get();

        Comment parent = null;
        if (requestDto.getParentId() != null) {
            // 자식댓글인 경우
            parent = commentRepository.findById(requestDto.getParentId()).get();
        }
        Comment comment = Comment.builder()
                .member(memberRepository.findByMemberId(requestDto.getMemberId()).get())
                .post(post)
                .content(requestDto.getContent())
                .build();

        if (null != parent) {
            comment.updateParent(parent);
        }
        Comment save = commentRepository.save(comment);

        return CommentDto.convertCommentToDto(save);
    }
}