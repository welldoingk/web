package com.kmpc.web.board.controller;

import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.repository.CustomPostRepository;
import com.kmpc.web.board.repository.PostImageRepository;
import com.kmpc.web.board.service.PostService;
import com.kmpc.web.common.entity.Code;
import com.kmpc.web.common.repository.CodeRepository;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.security.UserDetailsImpl;
import com.kmpc.web.util.CommonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/gallery")
@Controller
@RequiredArgsConstructor
public class MtController {

    private final CustomPostRepository customPostRepository;
    private final PostImageRepository postImageRepository;
    private final CodeRepository codeRepository;
    private final PostService postService;
    private final CommonUtil commonUtil;

    @GetMapping("/mt")
    public String mtList(@RequestParam Map<String,String> map, Pageable pageable, Model model) {
        Page<PostDto> results = customPostRepository.selectGalleryList(map, pageable, 3L);
        List<Code> codeList = codeRepository.findByClassCode("MT");

        model.addAttribute("list", results);
//        model.addAttribute("maxPage", 5);
//        model.addAttribute("totalCount", results.getTotalElements());
//        model.addAttribute("size", results.getPageable().getPageSize());
//        model.addAttribute("number", results.getPageable().getPageNumber());
        model.addAttribute("codeList", codeList);
        model.addAttribute("map", map);
        return "pages/MT/mtList";
    }

    @GetMapping("/recent")
    public String recentList(@RequestParam Map<String,String> map, Pageable pageable, Model model) {
        Page<PostDto> results = customPostRepository.selectGalleryList(map, pageable, 3L);
        List<Code> codeList = codeRepository.findByClassCode("MT");

        model.addAttribute("list", results);
//        model.addAttribute("maxPage", 5);
//        model.addAttribute("totalCount", results.getTotalElements());
//        model.addAttribute("size", results.getPageable().getPageSize());
//        model.addAttribute("number", results.getPageable().getPageNumber());
        model.addAttribute("codeList", codeList);
        model.addAttribute("map", map);
        return "pages/MT/mtList";
    }

    @GetMapping("/member")
    public String memberList(@RequestParam Map<String,String> map, Pageable pageable, Model model) {
        String mtNo = map.get("mtNo");
        Page<PostDto> results = customPostRepository.selectGalleryList(map, pageable, 3L);
        List<Code> codeList = codeRepository.findByClassCode("MT");

        model.addAttribute("list", results);
//        model.addAttribute("maxPage", 5);
//        model.addAttribute("totalCount", results.getTotalElements());
//        model.addAttribute("size", results.getPageable().getPageSize());
//        model.addAttribute("number", results.getPageable().getPageNumber());
        model.addAttribute("codeList", codeList);
        model.addAttribute("map", map);
        return "pages/MT/mtList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload")
    public String write(Model model, @PathVariable Long boardId) {
        Member member = commonUtil.getMember();

        String classCode = boardId == 1 ? "Notice" :  (boardId == 3 ? "MT" : null);
        List<Code> codeList = codeRepository.findByClassCode(classCode);

        PostDto postDto = new PostDto();
        postDto.setUsername(member.getMemberName());
        postDto.setBoardId(boardId);

        model.addAttribute("postDto", postDto);
        model.addAttribute("codeList", codeList);


        return "pages/MT/upload";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String save(@Valid PostDto postDto, BindingResult result,
            Model model) throws Exception {
        Member member = commonUtil.getMember();

        if (result.hasErrors()) {
            postDto.setUsername(member.getMemberName());
            return "pages/board/write";
        }

        Long l = postService.savePost(postDto);
        return"`redirect:/mt/detail/"+l;
    }

    @GetMapping("/detail/{postId}")
    public String detail(@PathVariable Long postId, Model model) {
        Post post = postService.selectPostDetail(postId);
        PostDto postDto = post.toDto();
        model.addAttribute("postDto", postDto);
        // model.addAttribute("postFile", customPostRepository.selectPostFileDetail(postId));
        model.addAttribute("postFile", postImageRepository.findByPost(post));

        return "pages/MT/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/board/update")
    public String update(@AuthenticationPrincipal UserDetailsImpl principal, @Valid PostDto postDto,
            BindingResult result) throws Exception {
        // 유효성검사 걸릴시
        if (result.hasErrors()) {
            return "pages/board/update";
        }

        postService.savePost(postDto);
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
