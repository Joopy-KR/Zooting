package com.zooting.api.domain.friend.dao;

import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@SpringBootTest
class FriendRequestRepositoryTest {

    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional
    @DisplayName("친구 요청 삭제 테스트")
    void deleteFriendRequestByFromAndToTest(){
        Member member1 = Member.builder().email("x").build();
        Member member2 = Member.builder().email("y").build();
        Member member3 = Member.builder().email("z").build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        FriendRequest friendRequest = FriendRequest.builder().from(member2).to(member1).build();
        FriendRequest friendRequest1 = FriendRequest.builder().from(member3).to(member1).build();
        friendRequestRepository.save(friendRequest); // 테스트 데이터베이스에 데이터 추가
        friendRequestRepository.save(friendRequest1);

        friendRequestRepository.deleteFriendRequestByFromAndTo(member2, member1);

        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
//        assertEquals(1, friendRequestList.size());
    } // 친구 요청 삭제

    @Test
    @Transactional
    @DisplayName("친구 요청 받은 리스트 테스트")
    void findByToTest(){
        Member member1 = Member.builder().email("x").build();
        Member member2 = Member.builder().email("y").build();
        Member member3 = Member.builder().email("z").build();
        Member member4 = Member.builder().email("w").build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        FriendRequest friendRequest = FriendRequest.builder().from(member2).to(member1).build();
        FriendRequest friendRequest1 = FriendRequest.builder().from(member3).to(member1).build();
        FriendRequest friendRequest2 = FriendRequest.builder().from(member4).to(member1).build();
        friendRequestRepository.save(friendRequest); // 테스트 데이터베이스에 데이터 추가
        friendRequestRepository.save(friendRequest1);
        friendRequestRepository.save(friendRequest2);

        List<FriendRequest> friendRequestList = friendRequestRepository.findByTo("x");

        // Then
        friendRequestList.stream().map(fr -> fr.getFrom().getEmail()).forEach(log::info);
//        assertNotNull(friendRequestList);
//        assertEquals(3, friendRequestList.size());

    } // 친구 요청 받은 리스트

    @Test
    @Transactional
    @DisplayName("친구 요청 보낸 리스트 테스트")
    void findByFromTest(){
        Member member1 = Member.builder().email("x").build();
        Member member2 = Member.builder().email("y").build();
        Member member3 = Member.builder().email("z").build();
        Member member4 = Member.builder().email("w").build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        FriendRequest friendRequest = FriendRequest.builder().from(member4).to(member1).build();
        FriendRequest friendRequest1 = FriendRequest.builder().from(member4).to(member2).build();
        FriendRequest friendRequest2 = FriendRequest.builder().from(member4).to(member3).build();
        friendRequestRepository.save(friendRequest); // 테스트 데이터베이스에 데이터 추가
        friendRequestRepository.save(friendRequest1);
        friendRequestRepository.save(friendRequest2);

        List<FriendRequest> friendRequestList = friendRequestRepository.findByFrom("w");

        // Then
        friendRequestList.stream().map(fr -> fr.getTo().getEmail()).forEach(log::info);
//        assertNotNull(friendRequestList);
//        assertEquals(3, friendRequestList.size());
    } // 친구 요청 보낸 리스트

}