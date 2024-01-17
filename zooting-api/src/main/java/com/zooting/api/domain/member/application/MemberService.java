package com.zooting.api.domain.member.application;

import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.member.entity.Member;

import java.util.List;

public interface MemberService {
    List<DMRoom> findByEmail(String sender);
}
