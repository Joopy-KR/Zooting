package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.dto.request.MemberReq;
import com.zooting.api.domain.member.entity.AdditionalInfo;
import com.zooting.api.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    @Override
    public boolean existNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
    @Override
    public void updateMemberInfo(MemberReq memberReq) {
        Optional<Member> member = memberRepository.findMemberByEmail(memberReq.nickname());
        if (member.isPresent()) {
            member.get().setAddress(memberReq.gender());
            member.get().setNickname(memberReq.nickname());
            member.get().setBirth(memberReq.birth());

            AdditionalInfo additionalInfo = new AdditionalInfo();
            additionalInfo.setPersonality(memberReq.personality());
            additionalInfo.setAnimal(memberReq.animal());
            additionalInfo.setInterest(memberReq.interest().toString());
            additionalInfo.setIdealAnimal(memberReq.ideal_animal().toString());

            member.get().setAdditionalInfo(additionalInfo);

            memberRepository.save(member.get());
        }
    }

}
