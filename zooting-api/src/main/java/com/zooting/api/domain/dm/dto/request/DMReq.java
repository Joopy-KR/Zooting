package com.zooting.api.domain.dm.dto.request;

import com.zooting.api.domain.file.entity.File;

public record DMReq(

    Long dmRoomId,
    String message,
    String sender,
    String receiver,
    //TODO: file
    File file
) {
}
