package com.kmpc.web.member.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kmpc.web.member.security.CustomUserDetailsService;
import com.kmpc.web.member.security.TokenProvider;
import com.kmpc.web.util.CookieUtil;
import com.kmpc.web.util.RedisUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        final Cookie jwtToken = cookieUtil.getCookie(httpServletRequest, TokenProvider.ACCESS_TOKEN_NAME);
//인증이 필요한 부분에 매번 이 필터가 실행된다고 보면 된다.
        String username = null;
        String jwt = null;
        String refreshJwt = null;
        String refreshUname = null;
// 액세스 토큰을 1차적으로 가져와서 쿠키에 저장한뒤에, 만료되었을시에 리프레쉬 토큰을 확인후 액세스토큰을 재발급 받는다.
        try {
            if (jwtToken != null) {
                jwt = jwtToken.getValue();
                username = tokenProvider.getUsername(jwt); //액세스토큰의 payload 에서 유저이름을 가져온다.
            } else {
                logger.warn("Cannot find access token");
            }
            if (username != null) { //액세스 토큰의 유저네임을 기반으로 인증을 거쳐 로그인을 유지해준다.
                System.out.println("userDetails : " + username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (tokenProvider.validateToken(jwt)) {
                    logger.info("validateToken : " + jwt); //토큰이 만료되지 않았다면 로그인을 유지시켜준다.
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } else {
                logger.warn("Cannot find username from access token");
            }
        } catch (ExpiredJwtException e) { //토큰이 만료되었을때 리프레쉬 토큰을 검증하여 액세스토큰을 재발급해준다.
            String refreshToken = cookieUtil.getCookie(httpServletRequest, TokenProvider.REFRESH_TOKEN_NAME).getValue();
            System.out.println("refreshToken : " + refreshToken);
            if (refreshToken != null) {
                refreshJwt = refreshToken;
            } else {
                logger.warn("Cannot find refresh token");
            }
        } catch (Exception e) {
        }
        try {
            if (refreshJwt != null) { //리프레쉬 토큰이 만료되지 않았을때 레디스에서 유저이름을 가져와 액세스토큰을 재발급 해주고 로그인을 유지시켜준다.
                refreshUname = redisUtil.getData(refreshJwt);
                if (refreshUname.equals(tokenProvider.getUsername(refreshJwt))) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(refreshUname);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities() );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    Cookie newAccessToken = cookieUtil.createCookie(TokenProvider.ACCESS_TOKEN_NAME, tokenProvider.doGenerateToken(refreshUname, TokenProvider.TOKEN_VALIDATION_SECOND));
                    httpServletResponse.addCookie(newAccessToken);
                }
            } else {
                logger.warn("Cannot find refresh token");
            }
        } catch (ExpiredJwtException e) {

        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}