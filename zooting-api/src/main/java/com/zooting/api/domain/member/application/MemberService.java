package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dto.request.InterestsReq;
import com.zooting.api.domain.member.dto.request.IntroduceReq;
import com.zooting.api.domain.member.dto.request.MemberReq;
import com.zooting.api.domain.member.dto.request.PersonalityReq;
import com.zooting.api.domain.member.dto.response.MembeSearchrRes;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.dto.response.PointRes;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.exception.BaseExceptionHandler;

import java.text.ParseException;
import java.util.List;

public interface MemberService {
    boolean existNickname(String nickname);
    boolean checkMemberPrivilege(String userId);
    MemberRes findMemberInfo(String userId);
    void updateMemberInfo(String memberId, MemberReq memberReq) throws ParseException, BaseExceptionHandler;
    void updateInterestsandIdeal(String memberId, InterestsReq additionalReq);
    void updateIntroduce(String memberId, IntroduceReq introduceReq);
    List<MembeSearchrRes> findMemberList(String userId, String nickname);
    void updatePersonality(String userId, PersonalityReq personalityReq);
    PointRes findPoints(String userId);
    Boolean deductPoints(String userId, Long price);
    Member initialMemberRegister(String email);
}
