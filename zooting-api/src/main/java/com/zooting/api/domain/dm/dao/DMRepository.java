package com.zooting.api.domain.dm.dao;

import com.zooting.api.domain.dm.entity.DM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DMRepository extends JpaRepository<DM, Long> {
    Page<DM> findByDmRoomIdAndIdLessThanOrderByIdDesc(Long dmRoomId, Long cursor, Pageable pageable);
}
