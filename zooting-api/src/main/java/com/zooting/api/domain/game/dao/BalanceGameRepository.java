package com.zooting.api.domain.game.dao;

import com.zooting.api.domain.game.entity.BalanceGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BalanceGameRepository extends JpaRepository<BalanceGame, Long> {
    @Query(value = "SELECT sentence1, sentence2 FROM balance_game ORDER BY RAND() limit 1", nativeQuery = true)
    Optional<BalanceGame> find();
}
