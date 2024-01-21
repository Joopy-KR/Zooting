package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dao.FriendRequestRepository;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService{

    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<FriendRes> getReceivedFriendRequests(String requestTo) {
        List<FriendRequest> receivedList = friendRequestRepository.findByTo(requestTo);
        List<FriendRes> friendResList = receivedList
                .stream()
                .map(friendRequest -> new FriendRes(friendRequest.getFrom().getNickname(), friendRequest.getFrom().getNickname()))
                .toList();
        return friendResList;
    }
    @Override
    public List<FriendRes> getSentFriendRequests(String requestFrom) {
        List<FriendRequest> sentList = friendRequestRepository.findByFrom(requestFrom);
        List<FriendRes> friendResList = sentList
                .stream()
                .map(friendRequest -> new FriendRes(friendRequest.getTo().getNickname(), friendRequest.getTo().getNickname()))
                .toList();
        return friendResList;
    }

    @Override
    public void sendFriendRequest(String requestFrom, String requestTo) {
        Member member1 = memberRepository.findByEmail(requestFrom)
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Member member2 = memberRepository.findByEmail(requestTo)
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        FriendRequest friendRequest = new FriendRequest(member1, member2);
        friendRequestRepository.save(friendRequest);
    }

}
