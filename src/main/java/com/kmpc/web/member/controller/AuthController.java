package com.kmpc.web.member.controller;

import com.kmpc.web.member.dto.LoginDto;
import com.kmpc.web.member.dto.MemberDto;
import com.kmpc.web.member.dto.TokenDto;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.security.TokenProvider;
import com.kmpc.web.member.security.jwt.JwtRequestFilter;
import com.kmpc.web.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;


    // @PostMapping("/authenticate")
    // public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

    //     UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

    //     // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
    //     Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    //     // 해당 객체를 SecurityContextHolder에 저장하고
    //     SecurityContextHolder.getContext().setAuthentication(authentication);
    //     // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
    //     String jwt = tokenProvider.generateToken(authentication);

    //     HttpHeaders httpHeaders = new HttpHeaders();
    //     // response header에 jwt token에 넣어줌
    //     httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

    //     // tokenDto를 이용해 response body에도 넣어서 리턴
    //     return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    // }


    @PostMapping("/signup")
    public ResponseEntity<Member> signup(@Valid MemberDto memberDto) {
        return ResponseEntity.ok(memberService.signup(memberDto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Member> getMyUserInfo() {
        return ResponseEntity.ok(memberService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Member> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(memberService.getUserWithAuthorities(username).get());
    }
}