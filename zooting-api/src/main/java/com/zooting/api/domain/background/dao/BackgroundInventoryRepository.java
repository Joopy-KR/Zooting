package com.zooting.api.domain.background.dao;

import com.zooting.api.domain.background.entity.Background;
import com.zooting.api.domain.background.entity.BackgroundInventory;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BackgroundInventoryRepository extends JpaRepository<BackgroundInventory, Long> {
    List<BackgroundInventory> findAllByMember(Member member);
}
