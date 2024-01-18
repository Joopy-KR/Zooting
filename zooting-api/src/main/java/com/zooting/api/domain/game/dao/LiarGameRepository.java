package com.zooting.api.domain.game.dao;

import com.zooting.api.domain.game.entity.LiarGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LiarGameRepository extends JpaRepository<LiarGame, Long> {
    @Query(value = "SELECT * FROM liar_game ORDER BY RAND() limit 1", nativeQuery = true)
    Optional<LiarGame> findTopic();

    @Query(value = "SELECT * FROM liar_game  WHERE topic = :topic ORDER BY RAND() limit 2", nativeQuery = true)
    Optional<List<LiarGame>> findWord(@Param("topic") String topic);
}
