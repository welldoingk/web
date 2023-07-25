package com.kmpc.web.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmpc.web.Member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
}
