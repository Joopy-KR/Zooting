package com.zooting.api.domain.friend.application;

import com.zooting.api.application.usecase.SendFriendUsecase;
import com.zooting.api.domain.friend.dao.FriendRequestRepository;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Log4j2
@SpringBootTest
class FriendRequestServiceTest {
    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private FriendService friendService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SendFriendUsecase sendFriendUsecase;

    @Test
    void getReceivedFriendRequestsTest() {
    }

    @Test
    void getSentFriendRequestsTest() {
    }

    @Test
    void sendFriendRequestTest() {
    }

    @Test
    @Transactional
    @WithMockUser(username = "x", roles = "USER")
    void rejectFriendRequestTest() {
        Member loginMember = Member.builder().email("x").build();
        Member deleteMember = Member.builder().email("y").build();
        sendFriendUsecase.sendFriendRequest("y", "x");
        friendRequestRepository.findAll().forEach(friendRequest -> log.info("{}, {}", friendRequest.getFrom().getEmail(), friendRequest.getTo().getEmail()));
        friendRequestService.rejectFriendRequest("x", "y");
        friendRequestRepository.findAll().forEach(friendRequest -> log.info("{}, {}", friendRequest.getFrom().getEmail(), friendRequest.getTo().getEmail()));
        assertEquals(0, friendRequestRepository.findByFrom("x").size());
    }

    @Test
    @Transactional
    @WithMockUser(username = "x", roles = "USER")
    void cancelFriendRequestTest() {
        Member loginMember = Member.builder().email("x").build();
        Member deleteMember = Member.builder().email("y").build();
        sendFriendUsecase.sendFriendRequest("x", "y");
        friendRequestRepository.findAll().forEach(friendRequest -> log.info("{}, {}", friendRequest.getFrom().getEmail(), friendRequest.getTo().getEmail()));
        friendRequestService.rejectFriendRequest("x", "y");
        friendRequestRepository.findAll().forEach(friendRequest -> log.info("{}, {}", friendRequest.getFrom().getEmail(), friendRequest.getTo().getEmail()));
        assertEquals(0, friendRequestRepository.findByTo("x").size());
    }
}