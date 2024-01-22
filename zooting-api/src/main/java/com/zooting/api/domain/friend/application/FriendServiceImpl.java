package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dao.FriendRequestRepository;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRepository;
    @Override
    public List<FriendRes> getFriends(String follower) {
        List<Friend> friendList = friendRepository.findFriendByFollower(follower);
        List<FriendRes> friendResList = friendList
                .stream()
                .map(friend -> new FriendRes(friend.getFollowing().getEmail(), friend.getFollowing().getNickname()))
                .toList();
        return friendResList;
    }

    @Override
    public List<FriendRes> searchFriend(String nickname, String loginEmail){
        //search friend contating nickname
        List<FriendRes> searchList = friendRepository.findByFollower_EmailAndFollowing_NicknameContaining
                        (loginEmail, nickname)
                .stream()
                .map(friend -> new FriendRes(friend.getFollowing().getEmail(), friend.getFollowing().getNickname()))
                .toList();
        return searchList;
    }
}
