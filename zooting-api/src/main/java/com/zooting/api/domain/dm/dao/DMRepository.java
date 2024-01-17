package com.zooting.api.domain.dm.dao;

import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DMRepository extends JpaRepository<DM, Long>{

    List<DM> findByDmRoomId(DMRoom dmRoom);
    List<File> getFilesById(Long dmId);

}
