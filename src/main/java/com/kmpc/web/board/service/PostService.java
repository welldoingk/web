package com.kmpc.web.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.repository.PostRepository;
import com.kmpc.web.file.service.FileService;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.repository.MemberRepository;
import com.kmpc.web.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Transactional
    public Long savePost(UserDetailsImpl principal, PostDto postDto) throws Exception{
        Post post = null;
        Member member = principal.getMember();
        //insert
        if(postDto.getId() == null){
            post = postDto.toEntity(memberRepository.findById(member.getMemberId()).get());
            postRepository.save(post);
        }

        //update
        else{
            post = postRepository.findById(postDto.getId()).get();
            post.update(postDto.getTitle(), postDto.getContent());
        }

        fileService.saveFile(postDto, post.getId());

        return post.getId();
    }

    @Transactional
    public Post selectPostDetail(Long id){
        Post post= postRepository.findById(id).get();
        post.updateViewCount(post.getViewCount());
        return post;
    }

    @Transactional
    public Post deleteBoard(Long id){
        Post post = postRepository.findById(id).get();

        //플래그값이 Y이면 논리삭제
        post.delete("Y");
        return post;
    }

}