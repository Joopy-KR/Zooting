package com.zooting.api.domain.dm.dao;

import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMRoomRepository extends JpaRepository<DMRoom, Long> {
    @Query("""
            SELECT dr FROM DMRoom dr
            WHERE (dr.sender = :sender AND dr.receiver = :receiver) OR
            (dr.sender = :receiver AND dr.receiver = :sender)
            """
            ) // sender, receiver 역방향도 검색
    DMRoom findBySenderAndReceiver(@Param("sender") Member sender, @Param("receiver") Member receiver);
    @Query("SELECT dm FROM DM dm WHERE dm.dmRoom.id = :id AND dm.id >= :startCursor")
    List<DM> findDmsById(@Param("id") Long id, @Param("startCursor") Long startCursor);
}
