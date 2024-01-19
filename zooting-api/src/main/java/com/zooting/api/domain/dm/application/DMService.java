package com.zooting.api.domain.dm.application;

import com.zooting.api.domain.dm.dto.DMDto;
import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.file.entity.File;
import com.zooting.api.domain.member.entity.Member;

import java.util.List;

public interface DMService {
    DMRoom      getDMRoom(String sender, String receiver);
    DMRoom      createDMRoom(String sender, String receiver);
    List<DM>    getDMList(DMRoom dmRoom);
    List<File>  getDmFiles(Long id);
    void        saveDM(DMDto dmDto);

}
