package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.dto.response.PointRes;

import java.text.ParseException;
import java.util.List;

public interface MemberService {
    boolean existNickname(String nickname);
    void updateMemberInfo(MemberReq memberReq) throws ParseException;
    void updateInterestsandIdeal(InterestsReq additionalReq);
    void updateIntroduce(IntroduceReq introduceReq);
    List<MemberRes> findMemberList(String email, String nickname);
    void updatePersonality(PersonalityReq personalityReq);
    void insertBlockList(BlockReq blockReq);
    void deleteBlock(BlockReq blockReq);
    void insertReport(ReportReq report);
    PointRes findPoints(String nickname);

}
