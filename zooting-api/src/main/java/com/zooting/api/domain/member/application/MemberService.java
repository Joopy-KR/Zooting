package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dto.request.InterestsReq;
import com.zooting.api.domain.member.dto.request.IntroduceReq;
import com.zooting.api.domain.member.dto.request.MemberReq;
import com.zooting.api.domain.member.dto.response.MemberRes;

import java.text.ParseException;
import java.util.List;

public interface MemberService {
    boolean existNickname(String nickname);
    void updateMemberInfo(MemberReq memberReq) throws ParseException;
    void updateInterestsandIdeal(InterestsReq additionalReq);
    void updateIntroduce(IntroduceReq introduceReq);
    List<MemberRes> findMemberList(String email, String nickname);
}
