package com.kmpc.web.board.controller;

import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.entity.PostImage;
import com.kmpc.web.board.repository.PostCustomRepository;
import com.kmpc.web.board.repository.PostImageRepository;
import com.kmpc.web.board.service.CommentService;
import com.kmpc.web.board.service.PostService;
import com.kmpc.web.common.entity.Code;
import com.kmpc.web.common.repository.CodeRepository;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.security.UserDetailsImpl;

import com.kmpc.web.util.CommonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final PostCustomRepository postCustomRepository;
    private final PostImageRepository postImageRepository;
    private final CodeRepository codeRepository;
    private final PostService postService;
    private final CommentService commentService;
    private final CommonUtil commonUtil;

    @GetMapping("/board")
    public String main(String searchVal, Pageable pageable,
            Model model) {
        model.addAttribute("list1", postCustomRepository.selectPostList(searchVal, pageable, 1L));
        model.addAttribute("list2", postCustomRepository.selectPostList(searchVal, pageable, 2L));
        return "pages/board/main";
    }

    @GetMapping("/board/{boardId}")
    public String list(@PathVariable Long boardId, String searchVal, Pageable pageable, Model model) {
        Page<PostDto> results = postCustomRepository.selectPostList(searchVal, pageable, boardId);
        model.addAttribute("list", results);
        model.addAttribute("boardId", boardId);
        model.addAttribute("maxPage", 5);
        model.addAttribute("totalCount", results.getTotalElements());
        model.addAttribute("size", results.getPageable().getPageSize());
        model.addAttribute("number", results.getPageable().getPageNumber());
        return "pages/board/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/write/{boardId}")
    public String write(Model model, @PathVariable Long boardId) {
        Member member = commonUtil.getMember();

        String classCode = boardId == 1 ? "Notice" :  (boardId == 3 ? "MT" : null);
        List<Code> codeList = codeRepository.findByClassCode(classCode);


        PostDto postDto = new PostDto();
        postDto.setUsername(member.getMemberName());
        postDto.setBoardId(boardId);

        model.addAttribute("postDto", postDto);
        model.addAttribute("codeList", codeList);

        if (boardId == 3) {
            return "pages/board/upload";
        }
        return "pages/board/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/board/write")
    public String save(@Valid PostDto postDto, BindingResult result,
            Model model) throws Exception {
        Member member = commonUtil.getMember();

        if (result.hasErrors()) {
            postDto.setUsername(member.getMemberName());
            return "pages/board/write";
        }

        PostDto savedPost = postService.savePost(postDto);
        model.addAttribute("postDto", savedPost);
        return "pages/board/detail";
    }

    @GetMapping("/board/detail/{postId}")
    public String detail(@PathVariable Long postId, Model model) {
        Post post = postService.selectPostDetail(postId);
        List<PostImage> postImages = postImageRepository.findByPost(post);
        List<CommentDto> commentDto = commentService.findCommentsByPostId(postId);
        PostDto postDto = post.toDto();

        model.addAttribute("postDto", postDto);
        // model.addAttribute("postFile", customPostRepository.selectPostFileDetail(postId));
        model.addAttribute("postFile", postImages);
        model.addAttribute("commentDto", commentDto);

        return "pages/board/detail";
    }

    @GetMapping("/board/update/{postId}")
    public String updatePage(@PathVariable Long postId, Model model) {
        Post post = postService.selectPostDetail(postId);
        List<PostImage> postImages = postImageRepository.findByPost(post);
        List<CommentDto> commentDto = commentService.findCommentsByPostId(postId);
        PostDto postDto = post.toDto();

        model.addAttribute("postDto", postDto);
        // model.addAttribute("postFile", customPostRepository.selectPostFileDetail(postId));
        model.addAttribute("postFile", postImages);
        model.addAttribute("commentDto", commentDto);

        return "pages/board/update";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/board/update")
    public String update(@AuthenticationPrincipal UserDetailsImpl principal, @Valid PostDto postDto,
            BindingResult result, Model model) throws Exception {
        // 유효성검사 걸릴시
        if (result.hasErrors()) {
            return "pages/board/update";
        }

        PostDto savedPost = postService.savePost(postDto);
        model.addAttribute("postDto", savedPost);
        return "pages/board/detail";
    }

    @PostMapping("/api/board/delete")
    public String delete(@RequestParam List<String> postIds) {

        for (int i = 0; i < postIds.size(); i++) {
            Long id = Long.valueOf(postIds.get(i));
            postService.deleteBoard(id);
        }

        return "redirect:/";
    }

}
