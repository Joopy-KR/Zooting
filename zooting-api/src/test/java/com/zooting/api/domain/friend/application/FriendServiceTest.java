package com.zooting.api.domain.friend.application;

import com.zooting.api.application.usecase.MemberAndFriendAndFriendRequestUsecase;
import com.zooting.api.application.usecase.MemberAndFriendRequestUsecase;
import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.entity.Friend;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private MemberAndFriendAndFriendRequestUsecase acceptUsecase;
    @Autowired
    private MemberAndFriendRequestUsecase memberAndFriendRequestUsecase;


//    @Test
//    @DisplayName("친구 수락 테스트")
//    @WithMockUser(username = "x", roles = "USER")
//    @Transactional
//    void acceptFriendTest(){
//        // Given
//        String fromEmail = "x";
//        String toEmail = "y";
//        memberRepository.save(Member.builder().email(fromEmail).nickname(fromEmail).build());
//        memberRepository.save(Member.builder().email(toEmail).nickname(toEmail).build());
//        memberAndFriendRequestUsecase.sendFriendRequest(fromEmail, toEmail);
//
//        UserDetails userDetails = getRoleUser(fromEmail);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        FriendReq friendReq = new FriendReq(fromEmail, toEmail);
//        // When
//        acceptUsecase.acceptFriend(userDetails.getUsername(), toEmail);
//
//        // Then
//        List<Friend> friendList = friendRepository.findAll();
//        friendList.forEach(friend -> log.info("{}, {}",friend.getFollower().getEmail(), friend.getFollowing().getEmail()));
//
//        assertEquals(toEmail, friendService.getFriends(fromEmail).get(0).email());
//        assertEquals(fromEmail, friendService.getFriends(toEmail).get(0).email());
//
//    }

    private static UserDetails getRoleUser(String fromEmail) {
        return User.builder()
                .username(fromEmail)
                .password(UUID.randomUUID().toString())
                .roles("USER")
                .build();
    }

//    @Test
//    @DisplayName("친구 검색 테스트")
//    @Transactional
//    void searchFriendTest(){
//        // Given
//        String fromEmail = UUID.randomUUID().toString();
//        String toEmail = UUID.randomUUID().toString();
//        Member member1 = memberRepository.save(Member.builder().email(fromEmail).nickname(fromEmail).build());
//        Member member2 = memberRepository.save(Member.builder().email(toEmail).nickname(toEmail).build());
//        Friend friend1 = Friend.builder().follower(member1).following(member2).build();
//        Friend friend2 = Friend.builder().follower(member2).following(member1).build();
//        friendRepository.save(friend1);
//        friendRepository.save(friend2);
//        friendService.getFriends(fromEmail)
//                .forEach(friend -> log.info("{}, {}", friend.email(), friend.nickname()));
//        // When
//        List<FriendRes> friendResList = friendService.searchFriend(toEmail.substring(0, toEmail.length()/2), fromEmail);
//
//        // Then
//        assertEquals(toEmail, friendResList.get(0).email());
//    }
}