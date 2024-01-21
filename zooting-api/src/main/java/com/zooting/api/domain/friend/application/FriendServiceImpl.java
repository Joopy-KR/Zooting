package com.zooting.api.domain.friend.application;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.dto.response.MemberRes;
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
    private final MemberRepository memberRepository;
    @Override
    public List<FriendRes> getFriends(String follower) {
        List<Friend> friendList = friendRepository.findFriendByFollower(follower);
        log.info(friendList);
        List<FriendRes> friendResList = friendList
                .stream()
                .map(friend -> new FriendRes(friend.getFollowing().getEmail(), friend.getFollowing().getNickname()))
                .toList();
        friendList.stream().map(friend -> friend.getFollowing().getEmail()).forEach(log::info);
        return friendResList;
    }

    @Override
    public void acceptFriend(FriendReq friendReq, Authentication authentication) {
        //양방향 저장
        Member member1 = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
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
    }

    @Override
    public List<MemberRes> searchFriend(String nickname) {
        //search friend contating nickname
        List<Member> memberList = memberRepository.findMemberByNicknameContaining(nickname);
        List<MemberRes> memberResList = memberList
                .stream()
                .map(member -> new MemberRes(member.getEmail(), member.getNickname()))
                .toList();
        return memberResList;
    }
}
