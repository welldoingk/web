package com.kmpc.web.board.repository;

import com.kmpc.web.board.dto.MtPostDto;
import com.kmpc.web.board.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PostCustomRepository {
    Page<PostDto> selectPostList(String searchVal, Pageable pageable, Long boardId);
    Page<MtPostDto> selectGalleryList(Map<String,String> map, Pageable pageable, Long boardId);
//    List<PostFileDto> selectPostFileDetail(Long postId);
}
