package com.zooting.api.domain.friend.dao;

import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@SpringBootTest
@RequiredArgsConstructor
class FriendRepositoryTest {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Test
    @DisplayName("친구 삭제 테스트")
    @Transactional
    void deleteFriendByFollowerAndFollowingTest() {
        // Given
        Member member1 = Member.builder().email("x").build();
        Member member2 = Member.builder().email("y").build();
        Member member3 = Member.builder().email("z").build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        Friend friend = Friend.builder().follower(member1).following(member2).build();
        Friend friend1 = Friend.builder().follower(member1).following(member3).build();
        friendRepository.save(friend); // 테스트 데이터베이스에 데이터 추가
        friendRepository.save(friend1);

        // When
        friendRepository.deleteFriendByFollowerAndFollowing(member1, member2);

        // Then
        List<Friend> friendList = friendRepository.findFriendByFollower("x");
        friendList.stream().map(fr -> fr.getFollowing().getEmail()).forEach(log::info);
        assertEquals(1, friendList.size());
    } // 친구 삭제

    @Test
    @DisplayName("본인 친구 목록 테스트")
    @Transactional
    void findFriendByFollowerTest() {
        // Given
        Member member1 = Member.builder().email("x").build();
        Member member2 = Member.builder().email("y").build();
        Member member3 = Member.builder().email("z").build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        Friend friend = Friend.builder().follower(member1).following(member2).build();
        Friend friend1 = Friend.builder().follower(member1).following(member3).build();
        friendRepository.save(friend); // 테스트 데이터베이스에 데이터 추가
        friendRepository.save(friend1);

        // When
        List<Friend> friendList = friendRepository.findFriendByFollower("x");

        // Then
        friendList.stream().map(fr -> fr.getFollowing().getEmail()).forEach(log::info);
        assertNotNull(friendList);
        assertEquals(2, friendList.size());
        assertEquals(member2.getEmail(), friendList.get(0).getFollowing().getEmail());
    }

}
