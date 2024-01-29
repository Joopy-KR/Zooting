package com.zooting.api.domain.background.dao;

import com.zooting.api.domain.background.entity.Background;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BackgroundRepository extends JpaRepository<Background, Long> {
    Page<Background> findBackgroundsBy(Pageable pageable);
    Optional<Background> findById(Long backgroundId);

}
