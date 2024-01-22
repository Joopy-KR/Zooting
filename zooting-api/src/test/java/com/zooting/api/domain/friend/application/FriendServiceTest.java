package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.friend.usecase.AcceptFriendUsecase;
import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
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


    @Test
    @DisplayName("친구 수락 테스트")
    @Transactional
    void acceptFriendTest(){
        // Given
        String fromEmail = "x";
        String toEmail = "y";
        memberService.initialMemberRegister(fromEmail);
        memberService.initialMemberRegister(toEmail);
        friendRequestService.sendFriendRequest(fromEmail, toEmail);

        Authentication authentication = new Authentication() {
            @Override
            public String getName() {
                return toEmail;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

        };
        FriendReq friendReq = new FriendReq(fromEmail, fromEmail);
        // When
        acceptUsecase.acceptFriend(friendReq, authentication);

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