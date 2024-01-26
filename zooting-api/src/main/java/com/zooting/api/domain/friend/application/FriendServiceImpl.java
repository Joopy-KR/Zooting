package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.entity.Friend;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
        return friendList
                .stream()
                .map(friend -> new FriendRes(friend.getFollowing().getEmail(), friend.getFollowing().getNickname(), friend.getFollowing().getAdditionalInfo().getAnimal(), friend.getFollowing().getGender()))
                .toList();
    }

    @Override
    public List<FriendRes> searchFriend(String nickname, String loginEmail){
        //search friend contating nickname
        return friendRepository.findByFollower_EmailAndFollowing_NicknameContaining
                        (loginEmail, nickname)
                .stream()
                .map(friend -> new FriendRes(friend.getFollowing().getEmail(), friend.getFollowing().getNickname(), friend.getFollowing().getAdditionalInfo().getAnimal(), friend.getFollowing().getGender()))
                .toList();
    }

}
