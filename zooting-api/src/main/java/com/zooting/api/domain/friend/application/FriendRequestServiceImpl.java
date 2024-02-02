package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dao.FriendRequestRepository;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.entity.FriendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public List<FriendRes> getReceivedFriendRequests(String requestTo) {
        List<FriendRequest> receivedList = friendRequestRepository.findByTo(requestTo);
        return receivedList
                .stream()
                .map(friendRequest -> new FriendRes(
                        friendRequest.getFrom().getEmail(),
                        friendRequest.getFrom().getNickname(),
                        Optional.ofNullable(friendRequest.getFrom().getAdditionalInfo())
                                .map(additionalInfo -> additionalInfo.getAnimal())
                                .orElse(null),
                        friendRequest.getFrom().getGender())
                )
                .toList();
    }

    @Override
    public List<FriendRes> getSentFriendRequests(String requestFrom) {
        List<FriendRequest> sentList = friendRequestRepository.findByFrom(requestFrom);
        return sentList
                .stream()
                .map(friendRequest -> new FriendRes(
                        friendRequest.getTo().getEmail(),
                        friendRequest.getTo().getNickname(),
                        Optional.ofNullable(friendRequest.getTo().getAdditionalInfo())
                                .map(additionalInfo -> additionalInfo.getAnimal())
                                .orElse(null),
                        friendRequest.getTo().getGender())
                )
                .toList();
    }

}
