package com.kmpc.web.member.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import com.kmpc.web.member.dto.MemberDto;
import com.kmpc.web.member.dto.MemberSessionDto;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.entity.Role;
import com.kmpc.web.member.repository.MemberRepository;
import com.kmpc.web.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public MemberSessionDto loginUser(String username, String password) {
          UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            MemberSessionDto principal1 = new MemberSessionDto(memberRepository.findByUsername(username).orElseThrow());
            return principal1;
    }


    @Transactional
    public Member signup(MemberDto memberDto) {
        if(memberRepository.findByUsername(memberDto.getUsername()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if(memberRepository.findByEmail(memberDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if(memberRepository.findByUsername(memberDto.getNickname()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        // 가입되어 있지 않은 회원이면,
        // 권한 정보 만들고
        Role role = Role.builder()
                .roleName("USER")
                .build();

        // 유저 정보를 만들어서 save
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .role(Collections.singleton(role))
                .build();

        return memberRepository.save(member);
    }

    // 유저,권한 정보를 가져오는 메소드
    @Transactional(readOnly = true)
    public Optional<Member> getUserWithAuthorities(String username) {
        return memberRepository.findOneWithAuthoritiesByUsername(username);
    }

//     현재 securityContext에 저장된 username의 정보만 가져오는 메소드
     @Transactional(readOnly = true)
     public Optional<Member> getMyUserWithAuthorities() {
         return SecurityUtil.getCurrentUsername()
                 .flatMap(memberRepository::findOneWithAuthoritiesByUsername);
     }
}