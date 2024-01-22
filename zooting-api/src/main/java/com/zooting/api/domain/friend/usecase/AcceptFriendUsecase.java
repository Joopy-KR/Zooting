package com.zooting.api.domain.friend.usecase;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dao.FriendRequestRepository;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AcceptFriendUsecase {
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;

    public void acceptFriend(FriendReq friendReq, Authentication authentication) {
        //양방향 저장
        Member member1 = Member.builder().email(authentication.getName()).build();
        Member member2 = memberRepository.findByEmail(friendReq.email())
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));

        Friend friend1 = new Friend();
        friend1.setFollower(member1);
        friend1.setFollowing(member2);
        Friend friend2 = new Friend();
        friend2.setFollower(member2);
        friend2.setFollowing(member1);
        friendRepository.save(friend1);
        friendRepository.save(friend2);
        friendRequestRepository.deleteFriendRequestByFromAndTo(member2, member1);
    }
}
