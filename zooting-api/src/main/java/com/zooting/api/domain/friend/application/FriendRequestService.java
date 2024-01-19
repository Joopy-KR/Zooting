package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dto.response.FriendRes;

import java.util.List;

public interface FriendRequestService {
    List<FriendRes> getReceivedFriendRequests(String requestTo);
    List<FriendRes> getSentFriendRequests(String requestFrom);

}
