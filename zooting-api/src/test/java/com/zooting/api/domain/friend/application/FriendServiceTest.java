package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.application.usecase.AcceptFriendUsecase;
import com.zooting.api.application.usecase.SendFriendUsecase;
import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class FriendServiceTest {
    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AcceptFriendUsecase acceptUsecase;
    @Autowired
    private SendFriendUsecase sendFriendUsecase;


    @Test
    @DisplayName("친구 수락 테스트")
    @WithMockUser(username = "x", roles = "USER")
    @Transactional
    void acceptFriendTest(){
        // Given
        String fromEmail = "x";
        String toEmail = "y";
        memberService.initialMemberRegister(fromEmail);
        memberService.initialMemberRegister(toEmail);
        sendFriendUsecase.sendFriendRequest(fromEmail, toEmail);

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            }
            @Override
            public String getPassword() {
                return "password"; // 실제 비밀번호로 교체해야 함
            }
            @Override
            public String getUsername() {
                return fromEmail;
            }
            @Override
            public boolean isAccountNonExpired() {
                return true; // 계정 만료되지 않음
            }
            @Override
            public boolean isAccountNonLocked() {
                return true; // 계정 잠겨있지 않음
            }
            @Override
            public boolean isCredentialsNonExpired() {
                return true; // 자격 증명 만료되지 않음
            }
            @Override
            public boolean isEnabled() {
                return true; // 계정 활성화됨
            }
        };

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        FriendReq friendReq = new FriendReq(toEmail, toEmail);
        // When
        acceptUsecase.acceptFriend(friendReq, userDetails);

        // Then
        List<Friend> friendList = friendRepository.findAll();
        friendList.forEach(friend -> log.info("{}, {}",friend.getFollower().getEmail(), friend.getFollowing().getEmail()));

        assertTrue(friendService.getFriends(fromEmail).size() == 1);
        assertTrue(friendService.getFriends(toEmail).size() == 1);

    } // 친구 수락

    @Test
    @DisplayName("친구 검색 테스트")
    @Transactional
    void searchFriendTest(){
        // Given
        String fromEmail = "abcdefg";
        String toEmail = "hijklmnop";
        Member member1 = memberRepository.save(Member.builder().email(fromEmail).nickname(fromEmail).build());
        Member member2 = memberRepository.save(Member.builder().email(toEmail).nickname(toEmail).build());
//        memberService.initialMemberRegister(fromEmail);
//        memberService.initialMemberRegister(toEmail);
        Friend friend1 = Friend.builder().follower(member1).following(member2).build();
        Friend friend2 = Friend.builder().follower(member2).following(member1).build();
        friendRepository.save(friend1);
        friendRepository.save(friend2);
        friendService.getFriends(fromEmail)
                .forEach(friend -> log.info("{}, {}", friend.email(), friend.nickname()));
        // When
        List<FriendRes> friendResList = friendService.searchFriend("o", fromEmail);

        // Then
        assertEquals(1, friendResList.size());
        assertEquals("hijklmnop", friendResList.get(0).email());
    } // 친구 검색
}