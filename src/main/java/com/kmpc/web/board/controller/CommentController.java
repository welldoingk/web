package com.kmpc.web.board.controller;

import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

//    @PostMapping("/{articleId}/reply")
//    public String writeChildrenComment(@PathVariable Long articleId, CommentDto dto) {
//        commentService.saveChildrenComment(dto.parentId(), dto.toDto(principal.toDto()));
//        return "redirect:/articles/" + articleId;
//    }

}
