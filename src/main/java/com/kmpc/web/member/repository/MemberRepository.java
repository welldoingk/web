package com.kmpc.web.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmpc.web.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findOneWithAuthoritiesByUsername(String username);

    Optional<Member> findByEmail(String email);
}
