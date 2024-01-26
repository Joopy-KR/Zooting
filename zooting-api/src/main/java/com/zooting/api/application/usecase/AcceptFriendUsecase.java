package com.zooting.api.application.usecase;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dao.FriendRequestRepository;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@PreAuthorize("hasAnyRole('USER')")
@Service
@RequiredArgsConstructor
public class AcceptFriendUsecase {
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;

    public void acceptFriend(FriendReq friendReq, UserDetails userDetails) {
        //양방향 저장
        Member member1 = Member.builder().email(userDetails.getUsername()).build();
        Member member2 = memberRepository.findByEmail(friendReq.email())
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));

        Friend friend1 = new Friend(member1, member2);
        Friend friend2 = new Friend(member2, member1);
        if(friendRepository.existsByFollowerAndFollowing(member1, member2) ||
                friendRepository.existsByFollowerAndFollowing(member2, member1)) {
            throw new BaseExceptionHandler(ErrorCode.ALREADY_EXIST_FRIEND);
        }
        friendRepository.saveAll(List.of(friend1, friend2));
        friendRequestRepository.deleteFriendRequestByFromAndTo(member2, member1);
    }
}
