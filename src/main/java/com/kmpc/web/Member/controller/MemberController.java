package com.kmpc.web.Member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/login")
    public String loginPage() {
        return "pages/member/signin";
    }

    @GetMapping("/join")
    public String join() {
        return "pages/member/signup";
    }
}
