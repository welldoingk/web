package com.kmpc.web.member.repository;

import com.kmpc.web.security.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kmpc.web.member.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByMemberId(String memberId);

    List<Member> findByRole(UserRoleEnum role);
    List<Member> findByRoleOrderByMemberIdAsc(UserRoleEnum role);
}