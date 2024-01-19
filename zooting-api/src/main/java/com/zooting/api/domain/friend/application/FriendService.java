package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.member.entity.Member;

import java.util.List;

public interface FriendService {
    List<Member> getFriends(String follower);

}
