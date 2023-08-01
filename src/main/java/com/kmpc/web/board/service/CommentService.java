//package com.kmpc.web.board.service;
//
//import com.kmpc.web.board.dto.CommentDto;
//import com.kmpc.web.board.entity.Comment;
//import com.kmpc.web.board.entity.Post;
//import com.kmpc.web.board.repository.CommentRepository;
//import com.kmpc.web.board.repository.PostRepository;
//import com.kmpc.web.util.CommonUtil;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class CommentService {
//
//    private final CommentRepository commentRepository;
//    private final PostRepository postRepository;
//    private final CommonUtil commonUtil;
//
//
//    @Transactional
//    public void saveComment(CommentDto commentDto) {
//
//        try {
//            Post post = postRepository.getReferenceById(commentDto.getId());
//            Comment comment = commentRepository.save(commentDto.toEntity(commonUtil.getMember(), post));
////            comment.setIsParent("Y");
//        } catch (EntityNotFoundException e) {
//            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
//        }
//    }
//
//    @Transactional
//    public void saveChildrenComment(Long parentId, CommentDto children) {
//        try {
//            Comment parent = commentRepository.getReferenceById(parentId);
//            Comment comment = children.toEntity(commonUtil.getMember(), parent.getPost());
//            commentRepository.save(comment);
//            comment.setIsParent("N");
//            comment.setParent(parent);
//            List<Comment> comments = parent.getChildren();
//            comments.add(comment);
//            parent.setChildren(comments);
//        } catch (EntityNotFoundException e) {
//            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
//        }
//    }
//}
