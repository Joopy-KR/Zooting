package com.zooting.api.domain.member.dao;

import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsBy(String nickname);

}
