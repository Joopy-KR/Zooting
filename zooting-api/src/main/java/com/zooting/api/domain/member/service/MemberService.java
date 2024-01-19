package com.zooting.api.domain.member.service;

import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {
    private final MemberRepository memberRepository;
    public Optional<Member> checkRegisteredMember (String email){
        return memberRepository.findByEmail(email);
    }
    public Member getMemberByEmail(String email){
        log.info(email + "정보 요청");
        return memberRepository.findByEmail(email)
                .orElseThrow(RuntimeException::new); //TODO
    }
    public Member initialMemberRegister(String email){
        return memberRepository.save(Member
                .builder()
                .email(email)
                .build());
    }
}
