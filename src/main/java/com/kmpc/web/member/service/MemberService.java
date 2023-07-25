package com.kmpc.web.member.service;

import com.kmpc.web.member.security.SecurityUtil;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Optional;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmpc.web.member.dto.MemberDto;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.entity.Role;
import com.kmpc.web.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Member signup(MemberDto memberDto) {
        if (memberRepository.findOneWithAuthoritiesByUsername(memberDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
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