package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.dto.response.FriendSearchPageRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FriendService {
    List<FriendRes> getFriends(String follower);
    FriendSearchPageRes searchFriend(Pageable pageable, String nickname, String loginEmail);
}
