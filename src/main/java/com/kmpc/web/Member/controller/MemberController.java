package com.kmpc.web.Member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    @GetMapping("/login")
    public String loginPage() {
        return "pages/member/signin";
    }

    @PostMapping("/login")
    public String login(){
        return "redirect:/";
    }


    @GetMapping("/join")
    public String join() {
        return "pages/member/signup";
    }
}
