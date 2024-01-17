package com.zooting.api.domain.member.service;

import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Optional<Member> getMemberByEmail(String email){
        return memberRepository.findByEmail(email);
    }
    public Member registerMember(Member member){
        return memberRepository.save(member);
    }
}
