package com.kmpc.web.board.controller;

import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.board.dto.MtPostDto;
import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.entity.PostFile;
import com.kmpc.web.board.repository.PostCustomRepository;
import com.kmpc.web.board.repository.PostFileRepository;
import com.kmpc.web.board.repository.PostRepository;
import com.kmpc.web.board.service.CommentService;
import com.kmpc.web.board.service.PostService;
import com.kmpc.web.common.entity.Code;
import com.kmpc.web.common.entity.CodeId;
import com.kmpc.web.common.repository.CodeRepository;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.repository.MemberRepository;
import com.kmpc.web.security.UserDetailsImpl;
import com.kmpc.web.security.UserRoleEnum;
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
public class GalleryController {

    private final PostCustomRepository postCustomRepository;
    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final CodeRepository codeRepository;
    private final PostService postService;
    private final CommentService commentService;
    private final CommonUtil commonUtil;
    private final MemberRepository memberRepository;

    @GetMapping("/mt")
    public String mtList(Map<String, String> map, Pageable pageable, Model model) {
        Page<MtPostDto> results = postCustomRepository.selectGalleryList(map, pageable, 3L, null);
        List<Code> codeList = codeRepository.findByClassCode("MT");

        map.put("mtNo", codeList.get(0).getCodeNo());
        model.addAttribute("list", results);
        model.addAttribute("codeList", codeList);
        model.addAttribute("map", map);
        return "pages/gallery/mtList";
    }

     @GetMapping("/mtAjax")
    @ResponseBody
    public Page<MtPostDto> mtListByAjax(@RequestParam Map<String, String> map, Pageable pageable, Model model) {
        Page<MtPostDto> results = postCustomRepository.selectGalleryList(map, pageable, 3L, null);
        return results;
    }

    @GetMapping("/recent")
    public String recentList(@RequestParam Map<String, String> map, Pageable pageable, Model model) {
        Page<MtPostDto> results = postCustomRepository.selectGalleryList(map, pageable, 3L, null);

        model.addAttribute("list", results);
        model.addAttribute("map", map);
        return "pages/gallery/recentList";
    }

    @GetMapping("/recentAjax")
    @ResponseBody
    public Page<MtPostDto> recentListByAjax(@RequestParam Map<String, String> map, Pageable pageable, Model model) {
        Page<MtPostDto> results = postCustomRepository.selectGalleryList(map, pageable, 3L, null);
        return results;
    }

    @GetMapping("/member")
    public String memberList(@RequestParam Map<String, String> map, Pageable pageable, Model model) {
        List<Member> memberList = memberRepository.findByRole(UserRoleEnum.VIP_MEMBER);
        String memberId;
        if (map.get("memberId") == null) {
            memberId = memberList.get(0).getMemberId();
            map.put("memberId", memberId);
        } else {
            memberId = map.get("memberId");
        }
        Page<MtPostDto> results = postCustomRepository.selectGalleryList(map, pageable, 3L, memberId);

        model.addAttribute("list", results);
        model.addAttribute("memberList", memberList);
        model.addAttribute("map", map);
        return "pages/gallery/memberList";
    }

    @GetMapping("/memberAjax")
    @ResponseBody
    public Page<MtPostDto> memberListByAjax(@RequestParam Map<String, String> map, Pageable pageable, Model model) {
        Page<MtPostDto> results = postCustomRepository.selectGalleryList(map, pageable, 3L, map.get("memberId"));
        return results;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload")
    public String uploadPage(Model model) {
        Member member = commonUtil.getMember();

        String classCode = "MT";
        List<Code> codeList = codeRepository.findByClassCode(classCode);

        MtPostDto mtPostDto = new MtPostDto();
        mtPostDto.setUsername(member.getMemberName());
        mtPostDto.setBoardId(3L);

        model.addAttribute("mtPostDto", mtPostDto);
        model.addAttribute("codeList", codeList);


        return "pages/gallery/upload";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload")
    public String save(@Valid MtPostDto mtPostDto, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            List<Code> codeList = codeRepository.findByClassCode("MT");
            model.addAttribute("codeList", codeList);
            return "pages/gallery/upload";
        }

        MtPostDto saveMtPost = postService.saveMtPost(mtPostDto);
        return "redirect:/gallery/member/detail/" + saveMtPost.getId();
    }

    @GetMapping("/{type}/detail/{postId}")
    public String detail(@PathVariable String type, @PathVariable Long postId, Model model) {
        Post post = postService.selectPostDetail(postId);
        Post nextPost = postRepository.findNextPost(postId);
        Post prevPost = postRepository.findPrevPost(postId);

        Code mt = codeRepository.findById(new CodeId("MT", post.getGbVal())).get();
        List<CommentDto> commentDto = commentService.findCommentsByPostId(postId);

        MtPostDto postDto = post.toMtDto();
        model.addAttribute("postDto", postDto);
        model.addAttribute("nextPost", nextPost);
        model.addAttribute("prevPost", prevPost);
        model.addAttribute("commentDto", commentDto);
        model.addAttribute("code", mt);
        model.addAttribute("type", type);
        model.addAttribute("postFile", postFileRepository.findByPost(post));

        return "pages/gallery/detail";
    }

    @GetMapping("/update/{postId}")
    public String updatePage(@PathVariable Long postId, Model model) {
        String classCode = "MT";
        List<Code> codeList = codeRepository.findByClassCode(classCode);
        Post post = postService.selectPostDetail(postId);
        List<PostFile> postFiles = postFileRepository.findByPost(post);
        List<CommentDto> commentDto = commentService.findCommentsByPostId(postId);
        PostDto mtPostDto = post.toDto();


        model.addAttribute("mtPostDto", mtPostDto);
        // model.addAttribute("postFile", customPostRepository.selectPostFileDetail(postId));
        model.addAttribute("postFile", postFiles);
        model.addAttribute("commentDto", commentDto);
        model.addAttribute("codeList", codeList);

        return "pages/gallery/update";
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
//
//    @PostMapping("/delete")
//    public String delete(@RequestParam List<String> postIds) {
//
//        for (int i = 0; i < postIds.size(); i++) {
//            Long id = Long.valueOf(postIds.get(i));
//            postService.deleteBoard(id);
//        }
//
//        return "redirect:/";
//    }

}
