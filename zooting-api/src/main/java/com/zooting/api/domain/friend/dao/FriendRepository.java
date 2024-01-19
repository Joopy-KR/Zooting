package com.zooting.api.domain.friend.dao;

import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findFriendByFollower(String follower);


}
