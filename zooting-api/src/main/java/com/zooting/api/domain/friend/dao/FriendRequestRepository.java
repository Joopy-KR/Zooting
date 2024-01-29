package com.zooting.api.domain.friend.dao;

import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    //delete request
    void deleteFriendRequestByFromAndTo(Member from, Member to);

    @Query("""
        SELECT fr FROM FriendRequest fr
        WHERE fr.to.email = :to
    """)
    List<FriendRequest> findByTo(@Param("to") String to); // 친구 요청 받은 리스트
    @Query("""
        SELECT fr FROM FriendRequest fr
        WHERE fr.from.email = :from
    """)
    List<FriendRequest> findByFrom(@Param("from") String from); // 친구 요청 보낸 리스트

    boolean existsByFromAndTo(Member fromMember, Member toMember);
}

