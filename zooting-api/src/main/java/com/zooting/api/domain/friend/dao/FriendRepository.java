package com.zooting.api.domain.friend.dao;

import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    void deleteFriendByFollowerAndFollowingOrFollowingAndFollower(Member follower1, Member following1, Member following2, Member follower2);
    Boolean existsByFollowerAndFollowing(Member follower, Member following);
}
