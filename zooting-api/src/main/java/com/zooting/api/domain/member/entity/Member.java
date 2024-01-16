package com.zooting.api.domain.member.entity;


import com.zooting.api.domain.animalface.entity.AnimalFace;
import com.zooting.api.domain.background.entity.BackgroundInventory;
import com.zooting.api.domain.disabled.entity.DisabledUser;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.mask.entity.MaskInventory;
import com.zooting.api.domain.report.entity.ReportList;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//@Builder
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
    @OneToOne
    private AnimalFace animalFace;

    @OneToMany
    private List<DMRoom> DMRooms;
    @OneToMany
    private List<BackgroundInventory> myBackgrounds;
    @OneToMany
    private List<MaskInventory> myMasks;
    @OneToMany
    private List<DisabledUser> disabledUsers;
    @OneToMany
    private List<ReportList> reportLists;

    @Builder
    public Member(String email, String gender, String nickname, Date birth, String address, Long point,
                  List<DMRoom> dmRooms, List<BackgroundInventory> myBackgrounds, List<MaskInventory> myMasks,
                  List<DisabledUser> disabledUsers, List<ReportList> reportLists) {
        this.email = email;
        this.gender = gender;
        this.nickname = nickname;
        this.birth = birth;
        this.address = address;
        this.point = point;
        this.status = true; // 회원 가입 시 회원 상태 true 고정

        this.DMRooms = Objects.nonNull(dmRooms) ? dmRooms : new ArrayList<>();
        this.myBackgrounds = Objects.nonNull(myBackgrounds) ? myBackgrounds : new ArrayList<>();
        this.myMasks = Objects.nonNull(myMasks) ? myMasks : new ArrayList<>();
        this.disabledUsers = Objects.nonNull(disabledUsers) ? disabledUsers : new ArrayList<>();
        this.reportLists = Objects.nonNull(reportLists) ? reportLists : new ArrayList<>();

    }
}
