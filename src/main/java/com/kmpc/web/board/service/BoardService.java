package com.kmpc.web.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.repository.PostRepository;
import com.kmpc.web.member.repositroy.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveBoard(PostDto postDto){
        Post post = null;
        //insert
        if(postDto.getId() == null){
            post = postDto.toEntity(memberRepository.findById(postDto.getId()).orElseThrow());
            postRepository.save(post);
        }

        //update
        else{
            post = postRepository.findById(postDto.getId()).get();
            post.update(postDto.getTitle(), postDto.getContent());
        }

        return post.getId();
    }

    @Transactional
    public Post deleteBoard(Long id){
        Post post = postRepository.findById(id).get();

        //플래그값이 Y이면 논리삭제
        post.delete("Y");
        return post;
    }

}