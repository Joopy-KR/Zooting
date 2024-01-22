package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dao.FriendRequestRepository;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService{
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public List<FriendRes> getReceivedFriendRequests(String requestTo) {
        List<FriendRequest> receivedList = friendRequestRepository.findByTo(requestTo);
        List<FriendRes> friendResList = receivedList
                .stream()
                .map(friendRequest -> new FriendRes(friendRequest.getFrom().getEmail(), friendRequest.getFrom().getNickname()))
                .toList();
        return friendResList;
    }
    @Override
    public List<FriendRes> getSentFriendRequests(String requestFrom) {
        List<FriendRequest> sentList = friendRequestRepository.findByFrom(requestFrom);
        List<FriendRes> friendResList = sentList
                .stream()
                .map(friendRequest -> new FriendRes(friendRequest.getTo().getEmail(), friendRequest.getTo().getNickname()))
                .toList();
        return friendResList;
    }



    @Override
    public void rejectFriendRequest(String requestFrom, String requestTo) {
        Member from = Member.builder().email(requestFrom).build(); // x
        Member to = Member.builder().email(requestTo).build(); // y
        friendRequestRepository.deleteFriendRequestByFromAndTo(to, from);
    }

}
