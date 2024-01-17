package com.zooting.api.domain.member.dao;

import com.zooting.api.domain.member.entity.AdditionalInfo;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByNickname(String nickname);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByNickname(String nickname);


}
