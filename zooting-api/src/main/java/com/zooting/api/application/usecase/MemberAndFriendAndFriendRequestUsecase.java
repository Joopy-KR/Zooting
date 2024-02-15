package com.zooting.api.application.usecase;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dao.FriendRequestRepository;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@PreAuthorize("hasAnyRole('USER')")
@Service
@RequiredArgsConstructor
public class MemberAndFriendAndFriendRequestUsecase {
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public String acceptFriend(String loginUserEmail, String nickname) {
        //양방향 저장
        Member member1 = Member.builder().email(loginUserEmail).build();
        Member member2 = memberRepository.findMemberByNickname(nickname)
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));

        Friend friend1 = new Friend(member1, member2);
        Friend friend2 = new Friend(member2, member1);
        if(friendRepository.existsByFollowerAndFollowing(member1, member2) ||
                friendRepository.existsByFollowerAndFollowing(member2, member1)) {
            throw new BaseExceptionHandler(ErrorCode.ALREADY_EXIST_FRIEND);
        }
        // 친구 정보 추가
        friendRepository.saveAll(List.of(friend1, friend2));
        // 친구 요청 정보 삭제
        friendRequestRepository.deleteFriendRequestByFromAndTo(member2, member1);
        friendRequestRepository.deleteFriendRequestByFromAndTo(member1, member2);

        return member2.getEmail();
    }
    @Transactional
    public void deleteFriend(String loginUserEmail, String nickname) {
        Member member1 = Member.builder().email(loginUserEmail).build();
        Member member2 = memberRepository.findMemberByNickname(nickname)
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        friendRepository.deleteFriendByFollowerAndFollowingOrFollowingAndFollower(member1, member2, member1, member2);
    }
    @Transactional
    public void rejectFriendRequest(String requestFrom, String requestTo) {
        Member from = Member.builder().email(requestFrom).build(); // x
        Member to = memberRepository.findMemberByNickname(requestTo)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        friendRequestRepository.deleteFriendRequestByFromAndTo(to, from);
    }
    @Transactional
    public void cancelFriendRequest(String requestFrom, String requestTo) {
        Member from = Member.builder().email(requestFrom).build();
        Member to = memberRepository.findMemberByNickname(requestTo)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        friendRequestRepository.deleteFriendRequestByFromAndTo(from, to);
    }
}
