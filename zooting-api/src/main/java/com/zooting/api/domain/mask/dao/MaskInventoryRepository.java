package com.zooting.api.domain.mask.dao;

import com.zooting.api.domain.mask.entity.MaskInventory;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaskInventoryRepository extends JpaRepository<MaskInventory, Long> {
    List<MaskInventory> findAllByMember(Member member);
    Boolean existsByMaskIdAndMember(Long maskId, Member member);
}
