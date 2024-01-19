package com.zooting.api.domain.friend.dao;

import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query("""
        SELECT fr FROM FriendRequest fr
        WHERE fr.to = :to
    """)
    List<FriendRequest> findByTo(String to); // 친구 요청 받은 리스트
    @Query("""
        SELECT fr FROM FriendRequest fr
        WHERE fr.from = :from
    """)
    List<FriendRequest> findByFrom(String from); // 친구 요청 보낸 리스트
}

