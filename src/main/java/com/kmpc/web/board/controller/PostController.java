package com.kmpc.web.board.controller;

import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.repository.CustomPostRepository;
import com.kmpc.web.board.service.PostService;
import com.kmpc.web.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final CustomPostRepository customPostRepository;
    private final PostService postService;

    @GetMapping("/board")
    public String main(@AuthenticationPrincipal UserDetailsImpl principal, String searchVal, Pageable pageable,
            Model model) {
        Page<PostDto> results = customPostRepository.selectPostList(searchVal, pageable);
        model.addAttribute("list", results);
        model.addAttribute("maxPage", 5);
        model.addAttribute("totalCount", results.getTotalElements());
        model.addAttribute("size", results.getPageable().getPageSize());
        model.addAttribute("number", results.getPageable().getPageNumber());
        return "pages/board/main";
    }

    @GetMapping("/board/")
    public String list(String searchVal, Pageable pageable, Model model) {
        Page<PostDto> results = customPostRepository.selectPostList(searchVal, pageable);
        model.addAttribute("list", results);
        model.addAttribute("maxPage", 5);
        model.addAttribute("totalCount", results.getTotalElements());
        model.addAttribute("size", results.getPageable().getPageSize());
        model.addAttribute("number", results.getPageable().getPageNumber());
        return "pages/board/list";
    }

    @GetMapping("/board/write")
    public String write(@AuthenticationPrincipal UserDetailsImpl principal, Model model) {
        PostDto postDto = new PostDto();
        postDto.setUsername(principal.getName());
        model.addAttribute("postDto", postDto);
        return "pages/board/write";
    }

    @PostMapping("/board/write")
    public String save(@AuthenticationPrincipal UserDetailsImpl principal, @Valid PostDto postDto, BindingResult result,
            Model model) throws Exception {

        if (result.hasErrors()) {
            postDto.setUsername(principal.getName());
            return "pages/board/write";
        }

        postService.savePost(principal, postDto);
        return "redirect:/";
    }

    @GetMapping("/board/{postId}")
    public String detail(@PathVariable Long postId, Model model) {
        Post post = postService.selectPostDetail(postId);
        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        model.addAttribute("postDto", postDto);
        model.addAttribute("postFile", customPostRepository.selectPostFileDetail(postId));

        return "pages/board/update";
    }

    @PutMapping("/update")
    public String update(@AuthenticationPrincipal UserDetailsImpl principal, @Valid PostDto postDto,
            BindingResult result) throws Exception {
        // 유효성검사 걸릴시
        if (result.hasErrors()) {
            return "pages/board/update";
        }

        postService.savePost(principal, postDto);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam List<String> postIds) {

        for (int i = 0; i < postIds.size(); i++) {
            Long id = Long.valueOf(postIds.get(i));
            postService.deleteBoard(id);
        }

        return "redirect:/";
    }

}