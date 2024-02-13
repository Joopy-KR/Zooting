package com.zooting.api.domain.background.dao;

import com.zooting.api.domain.background.entity.BackgroundInventory;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BackgroundInventoryRepository extends JpaRepository<BackgroundInventory, Long> {
    List<BackgroundInventory> findAllByMember_Email(String memberId);
    Boolean existsByBackgroundIdAndMember(Long backgroundId, Member member);
    List<BackgroundInventory> findByBackground_IdAndMember_Email(Long BackgroundId, String memberId);
}
