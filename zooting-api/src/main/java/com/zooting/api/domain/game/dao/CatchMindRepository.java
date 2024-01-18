package com.zooting.api.domain.game.dao;

import com.zooting.api.domain.game.entity.CatchMind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CatchMindRepository extends JpaRepository<CatchMind, Long> {
    @Query(value = "SELECT * FROM catch_mind ORDER BY RAND() limit 1", nativeQuery = true)
    Optional<CatchMind> find();
}
