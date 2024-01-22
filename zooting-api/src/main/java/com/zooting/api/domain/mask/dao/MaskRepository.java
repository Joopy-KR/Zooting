package com.zooting.api.domain.mask.dao;

import com.zooting.api.domain.mask.entity.Mask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaskRepository extends JpaRepository<Mask, Long> {
    List<Mask> findAll();
    Optional<Mask> findById(Long maskId);
}
