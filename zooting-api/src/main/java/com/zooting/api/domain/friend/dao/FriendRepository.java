package com.zooting.api.domain.friend.dao;

import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    void deleteFriendByFollowerAndFollowing(Member follower, Member following);

    @Query("""
        SELECT fr FROM Friend fr
        WHERE fr.follower = :follower
    """)
    List<Friend> findFriendByFollower(String follower);
}

