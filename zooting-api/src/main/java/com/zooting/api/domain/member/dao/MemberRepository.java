package com.zooting.api.domain.member.dao;

import com.zooting.api.domain.member.dto.response.MemberSearchRes;
import com.zooting.api.domain.member.dto.response.MemberStatus;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>, MemberRepositoryCustom {
    boolean existsByNickname(String nickname);

    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberByNickname(String nickname);
    Page<MemberSearchRes> findMembersBy(Pageable pageable);

    Page<MemberSearchRes> findMemberByNicknameContaining(Pageable pageable, String nickname);

    Page<MemberSearchRes> findByNicknameContainingAndNicknameNotIn(Pageable pageable, String nickname, List<String> nicknames);

    Optional<Member> findByEmail(String email);

    @Query(value = """
            select new com.zooting.api.domain.member.dto.response.MemberStatus(
            result.friend, result.block, result.report
            )
            FROM (
            SELECT
                    CASE WHEN
                        (SELECT COUNT(f.follower)
                         FROM Member m
                         JOIN Friend f ON f.follower = :me AND f.following = :you
                         WHERE m = :me) > 0
                    THEN true ELSE false END as friend,
                    CASE WHEN
                        (SELECT COUNT(b.to)
                         FROM Member m
                         JOIN Block b ON b.from = :me AND b.to = :you
                         WHERE m = :me) > 0
                    THEN true ELSE false END as block,
                    CASE WHEN
                        (SELECT COUNT(r.member)
                         FROM ReportList r
                         WHERE r.member = :you) > 0
                    THEN true ELSE false END as report
            FROM Member m
            WHERE m = :me
            ) as result
            """)
    MemberStatus findByNicknameWithStatus(@Param("me") Member me, @Param("you") Member you);
}