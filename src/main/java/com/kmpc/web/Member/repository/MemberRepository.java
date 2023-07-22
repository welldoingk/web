package com.kmpc.web.Member.repository;

import com.kmpc.web.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
