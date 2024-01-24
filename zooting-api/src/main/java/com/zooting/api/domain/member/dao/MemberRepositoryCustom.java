package com.zooting.api.domain.member.dao;

import com.zooting.api.domain.member.entity.Member;

import java.util.Date;
import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> extractMatchingMember(String userId, Date birth);
}
