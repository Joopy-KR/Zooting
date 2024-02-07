package com.zooting.api.global.security.userdetails.service;

import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.global.security.userdetails.CustomUserDetails;
import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findMemberByEmail(email);
        Member member = optionalMember.orElseGet(() -> registerMember(email));

        return CustomUserDetails.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .authorities(mapPrivilegeToAuthorities(member))
                .build();
    }

    private Member registerMember(String email) {
        return memberRepository.save(Member.builder()
                .email(email)
                .point(200L) // 시작시 초기 자금
                .role(List.of(Privilege.ANONYMOUS))
                .build());
    }

    protected Collection<? extends GrantedAuthority> mapPrivilegeToAuthorities(Member member) {
        return AuthorityUtils.createAuthorityList(member.getRole().stream().map(Enum::name).toList());
    }
}