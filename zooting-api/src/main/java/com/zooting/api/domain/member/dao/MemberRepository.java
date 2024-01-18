package com.zooting.api.domain.member.dao;

import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("SELECT m.dmRooms FROM Member m WHERE m.email = :email")
    List<DMRoom> findDMRoomsByEmail(@Param("email") String email);
    @Query("SELECT m.dmRoomsReverse FROM Member m WHERE m.email = :email")
    List<DMRoom> findDMRoomsReverseByEmail(@Param("email") String email);
}
