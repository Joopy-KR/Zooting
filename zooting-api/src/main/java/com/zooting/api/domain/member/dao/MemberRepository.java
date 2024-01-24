package com.zooting.api.domain.member.dao;

import com.zooting.api.domain.member.entity.AdditionalInfo;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>, MemberRepositoryCustom {
    boolean existsByNickname(String nickname);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByNickname(String nickname);
    List<Member> findMemberByNicknameContaining(String nickname);
    List<Member> findByNicknameContainingAndNicknameNotIn(String nickname, List<String> nicknames);
    Optional<Member> findByEmail(String email);
    @Query("SELECT m.dmRooms FROM Member m WHERE m.email = :email")
    List<DMRoom> findDMRoomsByEmail(@Param("email") String email);
    @Query("SELECT m.dmRoomsReverse FROM Member m WHERE m.email = :email")
    List<DMRoom> findDMRoomsReverseByEmail(@Param("email") String email);
}
