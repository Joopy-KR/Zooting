package com.zooting.api.domain.member.dao;

import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByNickname(String nickname);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByNickname(String nickname);
    List<Member> findMemberByNicknameContaining(String nickname);
    List<Member> findByNicknameContainingAndNicknameNotIn(String nickname, List<String> nicknames);
    Optional<Member> findByEmail(String email);
}
