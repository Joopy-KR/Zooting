package com.zooting.api.domain.background.dao;

import com.zooting.api.domain.background.entity.Background;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BackgroundRepository extends JpaRepository<Background, Long> {
    List<Background> findAll();
}
