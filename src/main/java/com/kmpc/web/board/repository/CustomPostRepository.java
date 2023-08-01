package com.kmpc.web.board.repository;

import com.kmpc.web.board.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    Page<PostDto> selectPostList(String searchVal, Pageable pageable, Long boardId);
//    List<PostFileDto> selectPostFileDetail(Long postId);
}
