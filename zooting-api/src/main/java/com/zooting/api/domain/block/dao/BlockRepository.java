package com.zooting.api.domain.block.dao;

import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    @Transactional
    void deleteBlockByFromAndTo(Member from, Member to);

    List<Block> findBlocksByFrom(Member me);
}
