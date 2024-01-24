package com.zooting.api.domain.member.dao;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Test
    @DisplayName("차단 유저, 친구 제외")
    @Transactional
    void extractNotFriendNotBlock() {
        Member member1 = Member.builder().email("aa@gmail.com").build();
        Member member2 = Member.builder().email("bb@gmail.com").build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        Friend friend1 = Friend.builder().follower(member1).following(member2).build();
        Friend friend2 = Friend.builder().follower(member2).following(member1).build();
        friendRepository.save(friend1);
        friendRepository.save(friend2);

//        List<Member> extractedMembers = memberRepository.extractMatchingMember(member1.getEmail());
//        assertNotNull(extractedMembers);
//        extractedMembers.stream().map(mem -> mem.getEmail()).forEach(log::info);

    }

}