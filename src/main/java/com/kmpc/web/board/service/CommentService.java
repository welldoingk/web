package com.kmpc.web.board.service;

import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.board.entity.Comment;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.repository.CommentRepository;
import com.kmpc.web.board.repository.PostRepository;
import com.kmpc.web.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommonUtil commonUtil;

    @Transactional
    public Long createComment(CommentDto requestDto, HttpServletRequest request) {


        Post post = postRepository.findById(requestDto.getPostId()).get();

        Comment parent = null;
        if (requestDto.getParentId() != null) {
            // 자식댓글인 경우
            parent = commentRepository.findById(requestDto.getParentId()).get();
        }
        Comment comment = Comment.builder()
                .member(commonUtil.getMember())
                .post(post)
                .content(requestDto.getContent())
                .build();

        if (null != parent) {
            comment.updateParent(parent);
        }
        commentRepository.save(comment);

        CommentDto commentDto = null;
        if (parent != null) {
            commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .memberName(comment.getMember().getMemberName())
                    .content(comment.getContent())
                    .parentId(comment.getParent().getId())
                    .build();
        } else {
            commentDto = commentDto.builder()
                    .id(comment.getId())
                    .memberName(comment.getMember().getMemberName())
                    .content(comment.getContent())
                    .build();
        }

        return commentDto.getId();
    }
}