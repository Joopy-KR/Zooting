package com.zooting.api.domain.friend.dao;

import com.zooting.api.domain.friend.dto.response.FriendSearchRes;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    void deleteFriendByFollowerAndFollowing(Member follower, Member following);

    @Query("""
                SELECT fr FROM Friend fr
                WHERE fr.follower.email = :follower
            """)
    List<Friend> findFriendByFollower(@Param("follower") String follower);

    int countByFollower_Email(String followerEmail);

    //searchFriend
    Page<Friend> findFriendsByFollower_EmailAndFollowing_NicknameContaining(Pageable pageable, String followerEmail, String nickname);

    List<Friend> findByFollower_EmailAndFollowing_NicknameContaining(String followerEmail, String nickname);


    void deleteFriendByFollowerAndFollowingOrFollowingAndFollower(Member follower1, Member following1, Member following2, Member follower2);

    Boolean existsByFollowerAndFollowing(Member follower, Member following);

}
