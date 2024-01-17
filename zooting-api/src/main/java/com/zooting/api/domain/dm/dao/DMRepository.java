package com.zooting.api.domain.dm.dao;

import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DMRepository extends JpaRepository<DM, Long>{

    List<DM> findByDmRoomId(DMRoom dmRoom);
    List<File> getFilesById(Long dmId);

}
