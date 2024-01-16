package com.zooting.api.domain.mask.entity;

import com.zooting.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaskInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mask_inventory_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "mask_id")
    private Mask mask;
    @ManyToOne
    @JoinColumn(name = "email")
    private Member member; // 유저 이메일 한개 당 여러 마스크 보유 가능

    @Builder
    public MaskInventory(Mask mask, Member member) {
        this.mask = mask;
        this.member = member;
    }
}
