package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.dto.response.PointRes;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.exception.BaseExceptionHandler;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface MemberService {
    boolean existNickname(String nickname);
    void updateMemberInfo(MemberReq memberReq) throws ParseException, BaseExceptionHandler;
    void updateInterestsandIdeal(InterestsReq additionalReq);
    void updateIntroduce(UserDetails userDetails, IntroduceReq introduceReq);
    List<MemberRes> findMemberList(String email, String nickname);
    void updatePersonality(PersonalityReq personalityReq);
    void insertBlockList(BlockReq blockReq);
    void deleteBlock(BlockReq blockReq);
    void insertReport(ReportReq report);
    PointRes findPoints(String nickname);
    Boolean deductPoints(String email, Long price);
    Member getMemberByEmail(String email);
    Member initialMemberRegister(String email);
    Optional<Member> checkRegisteredMember (String email);

}
