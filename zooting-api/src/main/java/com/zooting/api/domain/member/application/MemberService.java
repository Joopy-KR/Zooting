package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.*;


import java.text.ParseException;
import java.util.List;

public interface MemberService {
    boolean existNickname(String nickname);
    boolean checkMemberPrivilege(String userId);
    MyProfileReq checkMyProfile(String userId, String nickname);
    MemberRes findMemberInfo(String userId);
    MemberRes findMemberInfoByNickname(String nickname);
    void updateMemberInfo(String memberId, MemberReq memberReq) throws ParseException;
    void updateMemberInfo(String memberId, MemberModifyReq memberModifyReq);
    void updateInterests(String memberId, InterestsReq additionalReq);
    void updateIntroduce(String memberId, IntroduceReq introduceReq);
    List<MemberSearchRes> findMemberList(String userId, String nickname);
    void updatePersonality(String userId, PersonalityReq personalityReq);
    PointRes findPoints(String userId);
    Boolean deductPoints(String userId, Long price);
    List<MemberSearchRes> extractMembers(String userId, ExtractingReq extractingReq);
    List<MemberSearchRes> findMyBlockList(String userId);
}
