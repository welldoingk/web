package com.kmpc.web.board.controller;

import com.kmpc.web.board.dto.MtPostDto;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.repository.PostCustomRepository;
import com.kmpc.web.board.repository.PostImageRepository;
import com.kmpc.web.board.service.PostService;
import com.kmpc.web.common.entity.Code;
import com.kmpc.web.common.entity.CodeId;
import com.kmpc.web.common.repository.CodeRepository;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.repository.MemberRepository;
import com.kmpc.web.util.CommonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/gallery")
@Controller
@RequiredArgsConstructor
public class GalleryController {

    private final PostCustomRepository postCustomRepository;
    private final PostImageRepository postImageRepository;
    private final CodeRepository codeRepository;
    private final PostService postService;
    private final CommonUtil commonUtil;
    private final MemberRepository memberRepository;

    @GetMapping("/mt")
    public String mtList(@RequestParam Map<String,String> map, Pageable pageable, Model model) {
        Page<MtPostDto> results = postCustomRepository.selectGalleryList(map, pageable, 3L);
        List<Code> codeList = codeRepository.findByClassCode("MT");

        model.addAttribute("list", results);
//        model.addAttribute("maxPage", 5);
//        model.addAttribute("totalCount", results.getTotalElements());
//        model.addAttribute("size", results.getPageable().getPageSize());
//        model.addAttribute("number", results.getPageable().getPageNumber());
        model.addAttribute("codeList", codeList);
        model.addAttribute("map", map);
        return "pages/mt/mtList";
    }

    @GetMapping("/recent")
    public String recentList(@RequestParam Map<String,String> map, Pageable pageable, Model model) {
        Page<MtPostDto> results = postCustomRepository.selectGalleryList(map, pageable, 3L);
        List<Code> codeList = codeRepository.findByClassCode("MT");

        codeList.stream().forEach(a -> {
            if(Objects.equals(a.getCodeNo(), map.get("mtNo"))) map.put("mtName", a.getCodeName());
        });
        ;
        model.addAttribute("list", results);
//        model.addAttribute("maxPage", 5);
//        model.addAttribute("totalCount", results.getTotalElements());
//        model.addAttribute("size", results.getPageable().getPageSize());
//        model.addAttribute("number", results.getPageable().getPageNumber());
        model.addAttribute("codeList", codeList);
        model.addAttribute("map", map);
        return "pages/mt/mtList";
    }

    @GetMapping("/member")
    public String memberList(@RequestParam Map<String,String> map, Pageable pageable, Model model) {
        String mtNo = map.get("mtNo");
        Page<MtPostDto> results = postCustomRepository.selectGalleryList(map, pageable, 3L);
        List<Code> codeList = codeRepository.findByClassCode("MT");

        model.addAttribute("list", results);
//        model.addAttribute("maxPage", 5);
//        model.addAttribute("totalCount", results.getTotalElements());
//        model.addAttribute("size", results.getPageable().getPageSize());
//        model.addAttribute("number", results.getPageable().getPageNumber());
        model.addAttribute("codeList", codeList);
        model.addAttribute("map", map);
        return "pages/mt/mtList";
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


        return "pages/mt/upload";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload")
    public String save(@Valid MtPostDto mtPostDto, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            List<Code> codeList = codeRepository.findByClassCode("MT");
            model.addAttribute("codeList", codeList);
            return "pages/mt/upload";
        }

        MtPostDto saveMtPost = postService.saveMtPost(mtPostDto);
        return "redirect:/gallery/detail/"+saveMtPost.getId();
    }

    @GetMapping("/detail/{postId}")
    public String detail(@PathVariable Long postId, Model model) {
        Post post = postService.selectPostDetail(postId);
        Code mt = codeRepository.findById(new CodeId("MT", post.getGbVal())).get();
        MtPostDto postDto = post.toMtDto();
        model.addAttribute("postDto", postDto);
        model.addAttribute("code", mt);
        // model.addAttribute("postFile", customPostRepository.selectPostFileDetail(postId));
        model.addAttribute("postFile", postImageRepository.findByPost(post));

        return "pages/mt/detail";
    }

//    @PreAuthorize("isAuthenticated()")
//    @PutMapping("/board/update")
//    public String update(@AuthenticationPrincipal UserDetailsImpl principal, @Valid MtPostDto postDto,
//            BindingResult result) throws Exception {
//        // 유효성검사 걸릴시
//        if (result.hasErrors()) {
//            return "pages/board/update";
//        }
//
//        postService.savePost(postDto);
//        return "redirect:/";
//    }
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
