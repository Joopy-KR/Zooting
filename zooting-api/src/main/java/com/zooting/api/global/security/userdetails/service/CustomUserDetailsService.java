package com.zooting.api.global.security.userdetails;

import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.entity.Privilege;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 여기서 우리 쪽 유저로 전환
        // 1. 유저 정보가 없을 경우
        // -> 이메일 등록 후 익명 유저로 전환
        // 2. 유저 정보가 Anonymous일 경우
        // 3. 아닐 경우
        Optional<Member> member = memberRepository.findMemberByEmail(email);
        if (member.isPresent()) {
            List<Privilege> roles = member.get().getRole();
        } else {
            memberRepository.save(
                    Member.builder()
                            .email(email)
                            .role(new ArrayList<>(List.of(Privilege.ANONYMOUS)))
                            .build());
        }
    }
}