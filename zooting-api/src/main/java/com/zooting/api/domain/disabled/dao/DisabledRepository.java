package com.zooting.api.domain.disabled.dao;

import com.zooting.api.domain.disabled.entity.DisabledMember;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisabledRepository extends JpaRepository<DisabledMember, Long> {
    Optional<DisabledMember> findDisabledMemberByMember(Member member);
}
