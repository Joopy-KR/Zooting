package com.zooting.api.domain.dm.dto.response;

import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.file.entity.File;

import java.util.List;

public record DMRoomRes(
        Long dmRoomId,
        List<DMDto> dmList,
        Long    cursor

) {
}
