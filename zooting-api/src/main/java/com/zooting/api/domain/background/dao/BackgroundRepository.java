package com.zooting.api.domain.background.dao;

import com.zooting.api.domain.background.entity.Background;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BackgroundRepository extends JpaRepository<Background, Long> {
    List<Background> findAll();
    Optional<Background> findById(Long backgroundId);
}
