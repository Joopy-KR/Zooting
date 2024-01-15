package com.zooting.api.domain.animalface.entity;

import com.zooting.api.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@Entity
@NoArgsConstructor
// @Tag(description = "닮은 동물상 비율")
public class AnimalFace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="animalface_id")
    private Long id;
    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;
    private Long animal1;   // 강아지
    private Long animal2;   // 고양이
    private Long animal3;   // 토끼
    private Long animal4;   // 곰 또는 꼬부기
    private Long animal5;   // 공룡 또는 사슴

    @Builder
    public AnimalFace(Member member, Long animal1, Long animal2, Long animal3, Long animal4, Long animal5) {
        this.member = member;
        this.animal1 = animal1;
        this.animal2 = animal2;
        this.animal3 = animal3;
        this.animal4 = animal4;
        this.animal5 = animal5;
    }

}

