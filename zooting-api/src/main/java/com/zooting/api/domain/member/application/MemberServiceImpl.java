package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    @Override
    public boolean existNickname(String nickname) {
        return memberRepository.existsBy(nickname);
    }

    @Override
    public void saveAdditionalInfo(String email, String gender, String nickname, Date birth, String address) {
        Member member = memberRepository.findById(email).get();
        member.setGender(gender);
        member.setNickname(nickname);
        member.setBirth(birth);
        member.setAddress(address);
        memberRepository.save(member);
    }
}
