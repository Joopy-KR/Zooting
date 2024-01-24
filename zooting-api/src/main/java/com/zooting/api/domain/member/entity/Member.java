package com.zooting.api.domain.member.entity;


import com.zooting.api.domain.BaseEntity;
import com.zooting.api.domain.animalface.entity.AnimalFace;
import com.zooting.api.domain.background.entity.BackgroundInventory;
import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.mask.entity.MaskInventory;
import com.zooting.api.domain.meeting.entity.MeetingLog;
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
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    private String email;
    private String gender;
    @Column(unique = true)
    private String nickname;
    private Date birth;
    private String address;
    private Long point;
    private Boolean status;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private AdditionalInfo additionalInfo;
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_role",
            joinColumns = @JoinColumn(name = "member_id",
                    referencedColumnName = "email"))
    private List<Privilege> role;
    @OneToOne(mappedBy = "member")
    private AnimalFace animalFace;
    @OneToMany(mappedBy = "from", cascade = CascadeType.ALL)
    private List<Block> blockFromList;  // 내가 차단한 리스트
    @OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
    private List<Block> blockToList;  // 나를 차단한 리스트


    @OneToMany(mappedBy = "follower")
    private List<Friend> friendList;    // 내 친구 목록
    @OneToMany(mappedBy = "from")
    private List<FriendRequest> friendRequestFromMeList;  // 내가 친구요청 보낸 목록
    @OneToMany(mappedBy = "to")
    private List<FriendRequest> friendRequestToMeList;  // 나에게 온 친구 요청
    @OneToMany(mappedBy = "member")
    private List<MeetingLog> meetingLogList;   // 내 미팅 기록
    @OneToMany(mappedBy = "member")
    private List<BackgroundInventory> myBackgrounds;
    @OneToMany(mappedBy = "member")
    private List<MaskInventory> myMasks;
    @OneToMany(mappedBy = "sender")
    private List<DMRoom> dmRooms;
    @OneToMany(mappedBy = "receiver")
    private List<DMRoom> dmRoomsReverse;

    @Builder
    public Member(String email, String gender, String nickname, Date birth, String address, Long point,
                  List<BackgroundInventory> myBackgrounds, List<MaskInventory> myMasks, List<DMRoom> dmRooms,
                  List<DMRoom> dmRoomsReverse, List<Privilege> role) {
        this.email = email;
        this.gender = gender;
        this.nickname = nickname;
        this.birth = birth;
        this.address = address;
        this.point = point;
        this.status = true; // 회원 가입 시 회원 상태 true 고정
        this.myBackgrounds = Objects.nonNull(myBackgrounds) ? myBackgrounds : new ArrayList<>();
        this.myMasks = Objects.nonNull(myMasks) ? myMasks : new ArrayList<>();
        this.dmRooms = Objects.nonNull(dmRooms) ? dmRooms : new ArrayList<>();
        this.dmRoomsReverse = Objects.nonNull(dmRoomsReverse) ? dmRoomsReverse : new ArrayList<>();
        this.role = Objects.nonNull(role) ? role : new ArrayList<>();
    }
}
