package com.kmpc.web.board.controller;

import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.repository.CustomPostRepository;
import com.kmpc.web.board.service.BoardService;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final CustomPostRepository customPostRepository;
    private final BoardService boardService;

    @GetMapping("/board")
    public String main(@AuthenticationPrincipal Principal principal, String searchVal, Pageable pageable, Model model) {
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
    public String write(Model model){
        model.addAttribute("form", new PostDto());
        return "pages/board/write";
    }

    @PostMapping("/board/write")
    public String save(PostDto PostDto){
        boardService.saveBoard(PostDto);
        return "redirect:/";
    }

    @PatchMapping("update")
    public String update() {
        return "pages/board/update";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam List<String> boardIds){

        for(int i=0; i<boardIds.size(); i++){
            Long id = Long.valueOf(boardIds.get(i));
            boardService.deleteBoard(id);
        }

        return "redirect:/";
    }

}
