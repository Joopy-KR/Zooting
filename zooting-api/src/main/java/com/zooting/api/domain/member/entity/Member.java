package com.zooting.api.domain.member.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    private String email;
    private String gender;
    @Column(unique = true)
    private String nickname;
    private Date birth;
    private String address;
    private Long point;
    private Boolean status;
    @OneToOne
    private AdditionalInfo additionalInfo;
    @OneToOne
    private Privilege privilege;
//    @OneToOne
//    private AnimalFace animalFace;

    @Builder
    public Member(String email, String gender, String nickname, Date birth, String address, Long point) {
        this.email = email;
        this.gender = gender;
        this.nickname = nickname;
        this.birth = birth;
        this.address = address;
        this.point = point;
        this.status = true; // 회원 가입 시 회원 상태 true 고정
    }
}
