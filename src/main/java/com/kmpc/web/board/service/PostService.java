package com.kmpc.web.board.service;

import com.kmpc.web.board.entity.PostImage;
import com.kmpc.web.board.repository.PostImageRepository;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostImageRepository postImageRepository;
    private final CommonUtil memberUtil;
    private final S3Uploader s3Uploader;

    @Transactional
    public Long savePost(PostDto postDto) throws Exception{
        Post post = null;
        Member member = memberUtil.getMember();
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


        if(postDto.getPostFiles().isEmpty()) {
            List<String> postImages = uploadPostImages(postDto, post);
        }

//        fileService.saveFile(postDto, post.getId());

        return post.getId();
    }

     private List<String> uploadPostImages(PostDto postDto, Post post)  {
        return postDto.getPostFiles().stream()
                .map(image -> {
                    try {
                        return s3Uploader.upload(image, postDto.getBoardId() == 3 ? "MT" : "post");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(url -> createPostImage(post, url))
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());
    }

    private PostImage createPostImage(Post post, String url) {
        return postImageRepository.save(PostImage.builder()
                .imageUrl(url)
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