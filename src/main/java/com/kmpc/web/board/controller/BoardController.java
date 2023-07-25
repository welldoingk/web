package com.kmpc.web.board.controller;

import com.kmpc.web.board.dto.BoardDto;
import com.kmpc.web.board.repository.CustomBoardRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @Autowired
    CustomBoardRepository customBoardRepository;

    @GetMapping("/board")
    public String list(String searchVal, Pageable pageable, Model model) {
        Page<BoardDto> results = customBoardRepository.selectBoardList(searchVal, pageable);
        model.addAttribute("list", results);
        model.addAttribute("maxPage", 5);
        model.addAttribute("totalCount", results.getTotalElements());
        model.addAttribute("size", results.getPageable().getPageSize());
        model.addAttribute("number", results.getPageable().getPageNumber());
        return "pages/board/list2";
    }

    @GetMapping("write")
    public String write() {
        return "pages/board/write";
    }

    @PatchMapping("update")
    public String update() {
        return "pages/board/update";
    }

}
