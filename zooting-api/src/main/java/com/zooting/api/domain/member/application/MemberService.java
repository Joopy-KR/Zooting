package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.MemberSearchRes;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.dto.response.MyProfileReq;
import com.zooting.api.domain.member.dto.response.PointRes;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.exception.BaseExceptionHandler;

import java.text.ParseException;
import java.util.List;

public interface MemberService {
    boolean existNickname(String nickname);
    boolean checkMemberPrivilege(String userId);
    MyProfileReq checkMyProfile(String userId, String nickname);
    MemberRes findMemberInfo(String userId);
    MemberRes findMemberInfoByNickname(String nickname);
    void updateMemberInfo(String memberId, MemberReq memberReq) throws ParseException, BaseExceptionHandler;
    void updateInterestsandIdeal(String memberId, InterestsReq additionalReq);
    void updateIntroduce(String memberId, IntroduceReq introduceReq);
    List<MemberSearchRes> findMemberList(String userId, String nickname);
    void updatePersonality(String userId, PersonalityReq personalityReq);
    PointRes findPoints(String userId);
    Boolean deductPoints(String userId, Long price);
    Member initialMemberRegister(String email);
    List<MemberSearchRes> extractMembers(String userId, ExtractingReq extractingReq);
}
