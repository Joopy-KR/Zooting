package com.zooting.api.domain.friend.dao;

import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@SpringBootTest
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

    @Test
    @Transactional
    @DisplayName("친구 검색 테스트")
    void testFindByFollower_EmailAndFollowing_NicknameContaining() {
        Member member1 = new Member();
        member1.setEmail("user1@test.com");
        member1.setNickname("User1");
        Member member2 = new Member();
        member2.setEmail("user2@test.com");
        member2.setNickname("User2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Friend friend1 = new Friend(member1, member2);
        friendRepository.save(friend1);
        // Given
        String followerEmail = "user1@test.com";
        String nicknameSubstring = "2";

        // When
        List<Friend> friends = friendRepository.findByFollower_EmailAndFollowing_NicknameContaining(followerEmail, nicknameSubstring);

        // Then
        assertEquals(1, friends.size());
        Friend friend = friends.get(0);
        log.info("Follower: {}, Following: {}", friend.getFollower().getEmail(), friend.getFollowing().getEmail());

        // 추가로, 전체 Friend 데이터도 로그에 출력
        List<Friend> allFriends = friendRepository.findAll();
        allFriends.forEach(f -> log.info("All Friends: Follower: {}, Following: {}", f.getFollower().getEmail(), f.getFollowing().getEmail()));
    }
}