package com.kmpc.web.board.controller;

import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.board.dto.CommentRequestDto;
import com.kmpc.web.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 리스트 조회
    @ResponseBody
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> findAllComment(@PathVariable final Long postId) {
        return commentService.findCommentsByPostId(postId);
    }

    @ResponseBody
    @PostMapping("/api/comment/save")
    public CommentDto  save(@RequestBody final CommentRequestDto params, Model model) {
        CommentDto save = commentService.save(params);
        return save;
    }

    @ResponseBody
    @PostMapping("/api/comment/delete")
    public CommentDto  delete(@RequestBody final CommentRequestDto params, Model model) {
        CommentDto delete = commentService.delete(params);
        return delete;
    }
}
