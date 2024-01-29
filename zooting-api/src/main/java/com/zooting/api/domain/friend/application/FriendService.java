package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dto.response.FriendRes;

import java.util.List;

public interface FriendService {
    List<FriendRes> getFriends(String follower);
    List<FriendRes> searchFriend(String nickname, String loginEmail);
}
