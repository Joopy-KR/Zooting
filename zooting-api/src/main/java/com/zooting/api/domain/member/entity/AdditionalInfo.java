package com.zooting.api.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfo {
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
    private Long maskId;
    @Column(name = "background_id")
    private Long backgroundId;

    @Builder
    public AdditionalInfo(Member member, String personality, String animal, String introduce, String interest, String idealAnimal, Long maskId, Long backgroundId) {
        this.member = member;
        this.personality = personality;
        this.animal = animal;
        this.introduce = introduce;
        this.interest = interest;
        this.idealAnimal = idealAnimal;
        this.maskId = maskId;
        this.backgroundId = backgroundId;
    }

    public void setMember(Member member) {
        this.member = member;

        if (Objects.isNull(member.getAdditionalInfo()) ||
                member.getAdditionalInfo() != this) {
            member.setAdditionalInfo(this);
        }
    }
}
