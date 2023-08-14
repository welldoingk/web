package com.kmpc.web.configuration.handler;

import com.kmpc.web.jwt.JwtUtil;
import com.kmpc.web.security.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Value("${jwt.domain}")
    private String domain;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res,
                                        Authentication auth) throws IOException, ServletException {
        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER,
                jwtUtil.createToken(principal.getUsername(), principal.getMember().getRole() ));
        // cookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 유효
        cookie.setMaxAge(30 * 60);//
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setSecure(false);

        res.addCookie(cookie);
        res.sendRedirect("/");
    }
}