package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.*;
import com.zooting.api.domain.member.entity.Privilege;
import org.springframework.data.domain.Pageable;


import java.text.ParseException;
import java.util.List;

public interface MemberService {
    boolean existNickname(String nickname);
    List<Privilege> checkMemberPrivilege(String userId);
    MyProfileReq checkMyProfile(String userId, String nickname);
    MemberRes findMemberInfo(String userId);
    MemberRes findMemberInfoByNickname(String userId, String nickname);
    void updateMemberInfo(String memberId, MemberReq memberReq) throws ParseException;
    void updateMemberInfo(String memberId, MemberModifyReq memberModifyReq);
    void updateInterests(String memberId, InterestsReq additionalReq);
    void updateIntroduce(String memberId, IntroduceReq introduceReq);
    boolean changeMask(String memberId, MaskReq maskReq);
    void changeBackground(String memberId, BackgroundReq backgroundReq);
    boolean modifyNickname(String memberId, NicknameReq nicknameReq);
    MemberSearchPageRes findMemberList(Pageable pageable, String userId, String nickname);
    void updatePersonality(String userId, PersonalityReq personalityReq);
    PointRes findPoints(String userId);
    Boolean deductPoints(String userId, Long price);
    void addPoints(String userId, Long points);
    List<MemberSearchRes> extractMembers(String userId, ExtractingReq extractingReq);
    List<MemberSearchRes> findMyBlockList(String userId);
}
