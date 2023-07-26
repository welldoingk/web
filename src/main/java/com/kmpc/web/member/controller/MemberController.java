package com.kmpc.web.member.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.kmpc.web.member.dto.LoginDto;
import com.kmpc.web.member.dto.MemberSessionDto;
import com.kmpc.web.member.security.TokenProvider;
import com.kmpc.web.member.service.MemberService;
import com.kmpc.web.util.CookieUtil;
import com.kmpc.web.util.RedisUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @GetMapping("/login")
    public String login() {
        return "pages/member/signin";
    }

    @GetMapping("/join")
    public String join() {
        return "pages/member/signup";
    }

    @PostMapping("/login")
    public String login(LoginDto user, HttpServletRequest req, HttpServletResponse res) {
        try {
            final MemberSessionDto principal = memberService.loginUser(user.getUsername(), user.getPassword());
            Cookie accessToken = cookieUtil.createCookie(TokenProvider.ACCESS_TOKEN_NAME,
                    tokenProvider.generateToken(principal));
            accessToken.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(TokenProvider.TOKEN_VALIDATION_SECOND));
            Cookie refreshToken = cookieUtil.createCookie(TokenProvider.REFRESH_TOKEN_NAME,
                    tokenProvider.generateRefreshToken(principal));
            refreshToken.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(TokenProvider.REFRESH_TOKEN_VALIDATION_SECOND));
            redisUtil.setDataExpire(refreshToken.getValue(), principal.getUsername(), TokenProvider.REFRESH_TOKEN_VALIDATION_SECOND);
            res.addCookie(accessToken);
            res.addCookie(refreshToken);
            return "redirect:/";
        } catch (Exception e) {
            log.error("login error", e);
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        SecurityContextHolder.clearContext();
        Cookie accessToken = cookieUtil.getCookie(req, TokenProvider.ACCESS_TOKEN_NAME);
        Cookie refreshToken = cookieUtil.getCookie(req, TokenProvider.REFRESH_TOKEN_NAME);
        if (accessToken != null) {
            Long expiration = tokenProvider.getExpireTime(accessToken.getValue());
            redisUtil.setBlackList(accessToken.getValue(), "accessToken", expiration - System.currentTimeMillis());
            accessToken.setMaxAge(0);
            res.addCookie(accessToken);
        }
        if (refreshToken != null) {
            refreshToken.setMaxAge(0);
            res.addCookie(refreshToken);
            redisUtil.deleteData(refreshToken.getValue());
        }

        return "redirect:/";
    }

    

}
