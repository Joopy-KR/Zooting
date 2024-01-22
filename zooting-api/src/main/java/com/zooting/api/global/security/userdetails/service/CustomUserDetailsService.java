package com.zooting.api.global.security.userdetails.service;

import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.entity.Privilege;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findMemberByEmail(email);
        CustomUserDetails userDetails;

        if(member.isPresent()) {
            Member registeredMember = member.get();
            userDetails = CustomUserDetails.builder()
                    .email(registeredMember.getEmail())
                    .nickname(registeredMember.getNickname())
                    .authorities(mapPrivilegeToAuthorities(registeredMember))
                    .build();
        } else { // 최초 등록하는 유저일 경우 DB에 저장 후 userDetails로 매핑
            Member newMember = memberRepository.save(Member.builder()
                            .email(email)
                            .role(List.of(Privilege.ANONYMOUS))
                            .build());

            userDetails = CustomUserDetails.builder()
                    .email(newMember.getEmail())
                    .authorities(mapPrivilegeToAuthorities(newMember))
                    .build();
        }
        return userDetails;
    }

    protected Collection<? extends GrantedAuthority> mapPrivilegeToAuthorities(Member member){
        return AuthorityUtils.createAuthorityList(member.getRole().stream().map(Enum::name).toList());
    }
}