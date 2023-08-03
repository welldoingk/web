package com.kmpc.web.board.repository;

import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.entity.Post;

import java.util.List;

public interface CommentCustomRepository {
    List<CommentDto> getAllCommentsByPost(Post post);
}
