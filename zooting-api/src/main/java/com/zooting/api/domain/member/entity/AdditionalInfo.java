package com.zooting.api.domain.member.entity;

import com.zooting.api.domain.BaseEntity;
import com.zooting.api.domain.background.entity.Background;
import com.zooting.api.domain.background.entity.BackgroundInventory;
import com.zooting.api.domain.mask.entity.Mask;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "additional_info")
public class AdditionalInfo extends BaseEntity {
    @Id
    @Column(name = "additional_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "email")
    private Member member;
    private String personality;
    private String animal;
    private String introduce;
    private String interest;
    @Column(name = "ideal_animal")
    private String idealAnimal;
    private String maskUrl;
    private String backgroundUrl;

    @Builder
    public AdditionalInfo(Member member, String personality, String animal, String introduce, String interest, String idealAnimal, String  maskUrl, String backgroundUrl) {
        this.member = member;
        this.personality = personality;
        this.animal = animal;
        this.introduce = introduce;
        this.interest = interest;
        this.idealAnimal = idealAnimal;
        this.maskUrl = maskUrl;
        this.backgroundUrl = backgroundUrl;
    }

    public void setMember(Member member) {
        this.member = member;

        if (Objects.isNull(member.getAdditionalInfo()) ||
                member.getAdditionalInfo() != this) {
            member.setAdditionalInfo(this);
        }
    }
}
