package com.zooting.api.domain.mask.dao;

import com.zooting.api.domain.mask.entity.Mask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface MaskRepository extends JpaRepository<Mask, Long> {
    Page<Mask> findMasksBy(Pageable pageable);
    Page<Mask> findMasksByAnimal(Pageable pageable, String animal);
    Optional<Mask> findById(Long maskId);

}
