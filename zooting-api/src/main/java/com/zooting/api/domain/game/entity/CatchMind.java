package com.zooting.api.domain.game.entity;

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
public class CatchMind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="catchmind_id")
    private Long id;
    private String word;

}
