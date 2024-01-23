package com.zooting.api.domain.block.entity;

import com.zooting.api.domain.BaseEntity;
import com.zooting.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "block")
public class Block extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="block_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "block_from", referencedColumnName = "email")
    private Member from;
    @ManyToOne
    @JoinColumn(name = "block_to", referencedColumnName = "email")
    private Member to;

    @Builder
    public Block(Member from, Member to) {
        this.from = from;
        this.to = to;
    }


}
