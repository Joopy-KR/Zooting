package com.zooting.api.domain.dm.application;

import com.zooting.api.domain.dm.dto.request.DMReq;
import com.zooting.api.domain.dm.dto.response.DMRoomRes;
import com.zooting.api.domain.dm.dto.response.RedisDMRoomRes;
import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DMService {
    DMRoom getDMRoom(String sender, String receiver);

    DMRoom createDMRoom(String sender, String receiver);

    List<DM> getAllDMList(Long dmRoomId, Long start);

    Page<DM> getDMList(Long dmRoomId, Long cursor);

    void saveDM(DMReq dmReq);

    DMRoomRes enterDMRoom(String sender, String receiver);

    RedisDMRoomRes enterDMRoomRedis(String sender, String receiver);
    DMRoomRes getDMRoomWithCursor(Long dmRoomId, Long cursor);

    void exitDmRoom(Long dmRoomId, String loginEmail);
    RedisDMRoomRes getDMRoomWithCursorRedis(Long dmRoomId, Long cursor);
}
