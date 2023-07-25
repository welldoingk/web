package com.kmpc.web.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kmpc.web.member.dto.MemberDto;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    // @GetMapping("/login")
    // public String loginPage() {
    //     return "pages/member/signin";
    // }

    // @PostMapping("/login")
    // public String login(){
    //     return "redirect:/";
    // }


    // @GetMapping("/join")
    // public String join() {
    //     return "pages/member/signup";
    // }

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Member> signup(
            @Valid @RequestBody MemberDto memberDto
    ) {
        return ResponseEntity.ok(memberService.signup(memberDto));
    }

    // @GetMapping("/user")
    // @PreAuthorize("hasAnyRole('USER','ADMIN')")
    // public ResponseEntity<Member> getMyUserInfo() {
    //     return ResponseEntity.ok(memberService.getMyUserWithAuthorities().get());
    // }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Member> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(memberService.getUserWithAuthorities(username).get());
    }
}
