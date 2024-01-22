package com.zooting.api.domain.game.entity;

import com.zooting.api.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "balance_game")
public class BalanceGame  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="balancegame_id")
    private Long id;
    private String sentence1;
    private String sentence2;
}
