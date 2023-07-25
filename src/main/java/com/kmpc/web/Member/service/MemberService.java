package com.kmpc.web.Member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kmpc.web.Member.dto.MemberDto;
import com.kmpc.web.Member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Long join(MemberDto dto) {
        dto.setPassword(encoder.encode(dto.getPassword()));

        return memberRepository.save(dto.toEntity()).getId();
    }

}