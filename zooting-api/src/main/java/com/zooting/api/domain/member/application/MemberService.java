package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dto.request.MemberReq;

public interface MemberService {
    boolean existNickname(String nickname);
    void updateMemberInfo(MemberReq memberReq);
}
