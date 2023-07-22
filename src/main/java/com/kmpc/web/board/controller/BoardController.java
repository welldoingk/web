package com.kmpc.web.board.controller;

import com.kmpc.web.board.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @Autowired

    @GetMapping("/board")
    public String list() {
//        Page<BoardDto> results = customBoardRepository.selectBoardList(searchVal, pageable);
//        model.addAttribute("list", results);
//        model.addAttribute("maxPage", 5);
//        pageModelPut(results, model);
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
