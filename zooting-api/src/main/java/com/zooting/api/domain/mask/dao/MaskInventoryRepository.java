package com.zooting.api.domain.mask.dao;

import com.zooting.api.domain.mask.entity.Mask;
import com.zooting.api.domain.mask.entity.MaskInventory;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaskInventoryRepository extends JpaRepository<MaskInventory, Long> {
    List<MaskInventory> findAllByMember_Email(String memberId);
    Boolean existsByMaskIdAndMember(Long maskId, Member member);
    List<MaskInventory> findByMask_IdAndMember_Email(Long maskId, String memberId);
}
