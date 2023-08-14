package com.kmpc.web.board.service;

import com.kmpc.web.board.dto.MtPostDto;
import com.kmpc.web.board.entity.PostFile;
import com.kmpc.web.board.repository.PostFileRepository;
import com.kmpc.web.util.CommonUtil;
import com.kmpc.web.util.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.repository.PostRepository;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostFileRepository postFileRepository;
    private final CommonUtil memberUtil;
    private final S3Uploader s3Uploader;

    @Transactional
    public PostDto savePost(PostDto postDto) throws Exception{
        Post post = null;
        Member member = memberRepository.findByMemberId(postDto.getMemberId()).get();
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


        int checkNum =1;
        for(MultipartFile image:postDto.getPostFiles()){
            if(image.isEmpty()) checkNum=0;
        }
        if(checkNum==1) {
            List<String> postImages = uploadPostFiles(postDto, post);
        }

//        fileService.saveFile(postDto, post.getId());

        return post.toDto();
    }

    @Transactional
    public MtPostDto saveMtPost(MtPostDto mtPostDto) throws Exception{
        Post post = null;
        Member member = memberUtil.getMember();
        //insert
        if(mtPostDto.getId() == null){
            post = mtPostDto.toEntity(memberRepository.findById(member.getMemberId()).get());
            postRepository.save(post);
        }

        //update
        else{
            post = postRepository.findById(mtPostDto.getId()).get();
            post.update(mtPostDto.getTitle(), mtPostDto.getContent());
        }

        int checkNum =1;
        for(MultipartFile image:mtPostDto.getPostFiles()){
            if(image.isEmpty()) checkNum=0;
        }
        if(checkNum==1) {
            List<String> postImages = uploadPostFiles(mtPostDto, post);
        }

//        fileService.saveFile(postDto, post.getId());

        return post.toMtDto();
    }

     private List<String> uploadPostFiles(PostDto postDto, Post post)  {
        return postDto.getPostFiles().stream()
                .map(file -> {
                    try {
                        return s3Uploader.upload(file, postDto.getBoardId() == 3 ? "MT" : "post");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(url -> createPostImage(post, url))
                .map(PostFile::getFileUrl)
                .collect(Collectors.toList());
    }

    private List<String> uploadPostFiles(MtPostDto mtPostDto, Post post)  {
        return mtPostDto.getPostFiles().stream()
                .map(image -> {
                    try {
                        return s3Uploader.upload(image, mtPostDto.getBoardId() == 3 ? "MT" : "post");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(url -> createPostImage(post, url))
                .map(PostFile::getFileUrl)
                .collect(Collectors.toList());
    }

    private PostFile createPostImage(Post post, String url) {
        return postFileRepository.save(PostFile.builder()
                .fileUrl(url)
                .storeFilename(StringUtils.getFilename(url))
                .post(post)
                .build());
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