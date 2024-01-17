package com.zooting.api.domain.dm.dao;

import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DMRoomRepository extends JpaRepository<DMRoom, Long> {
    DMRoom findBySenderAndReceiver(Member sender, Member receiver);
}
