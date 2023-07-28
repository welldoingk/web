package com.kmpc.web.board.repository;

import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.file.dto.PostFileDto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    Page<PostDto> selectPostList(String searchVal, Pageable pageable);
    List<PostFileDto> selectPostFileDetail(Long postId);
}
