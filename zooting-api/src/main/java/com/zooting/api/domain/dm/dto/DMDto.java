package com.zooting.api.domain.dm.dto;

import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.file.entity.File;
import com.zooting.api.domain.member.entity.Member;

public record DMDto(

    Long dmRoomId,
    String roomId,
    String message,
    Boolean status,
    String sender,
    String receiver,
    File file
) {
}
