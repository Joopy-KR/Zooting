package com.zooting.api.domain.background.entity;

import com.zooting.api.domain.BaseEntity;
import com.zooting.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "background_inventory")
public class BackgroundInventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "background_id")
    private Background background;
    @ManyToOne
    @JoinColumn(name = "email")
    private Member member; // 유저 이메일 한개 당 여러 마스크 보유 가능

    @Builder
    public BackgroundInventory(Background background, Member member) {
        this.background = background;
        this.member = member;
    }
}
