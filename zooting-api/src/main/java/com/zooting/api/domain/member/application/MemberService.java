package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.dto.response.PointRes;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.exception.BaseExceptionHandler;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface MemberService {
    boolean existNickname(String nickname);
    void updateMemberInfo(String memberId, MemberReq memberReq) throws ParseException, BaseExceptionHandler;
    void updateInterestsandIdeal(String memberId, InterestsReq additionalReq);
    void updateIntroduce(String memberId, IntroduceReq introduceReq);
    List<MemberRes> findMemberList(String userId, String nickname);
    void updatePersonality(String userId, PersonalityReq personalityReq);
    void insertBlockList(String userId, BlockReq blockReq);
    void deleteBlock(String userId, BlockReq blockReq);
    void insertReport(String userId, ReportReq report);
    PointRes findPoints(String userId);
    Boolean deductPoints(String userId, Long price);
    Member getMemberByEmail(String email);
    Member initialMemberRegister(String email);
    Optional<Member> checkRegisteredMember (String email);

}
