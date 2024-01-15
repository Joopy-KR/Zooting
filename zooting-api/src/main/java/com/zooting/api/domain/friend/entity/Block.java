package com.zooting.api.domain.friend.entity;
import com.zooting.api.domain.member.entity.Member;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
//Tag(description = "차단된 유저")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="block_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "block_from")
    private Member from;
    @ManyToOne
    @JoinColumn(name = "block_to")
    private Member to;

    @Builder
    public Block(Member from, Member to) {
        this.from = from;
        this.to = to;
    }
}
