package com.zooting.api.domain.member.entity;


import com.zooting.api.domain.animalface.entity.AnimalFace;
import com.zooting.api.domain.background.entity.BackgroundInventory;
import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.mask.entity.MaskInventory;
import com.zooting.api.domain.meeting.entity.MeetingLog;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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

    @OneToOne(mappedBy = "member")
    private AdditionalInfo additionalInfo;
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_role",
            joinColumns = @JoinColumn(name = "member_id",
                    referencedColumnName = "email"))
    private List<Privilege> role;
    @OneToOne(mappedBy = "member")
    private AnimalFace animalFace;
    @OneToMany(mappedBy = "from")
    private List<Block> blockList;  // 내가 차단한 리스트
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

    @Builder
    public Member(String email, String gender, String nickname, Date birth, String address, Long point,
                  List<BackgroundInventory> myBackgrounds, List<MaskInventory> myMasks) {
        this.email = email;
        this.gender = gender;
        this.nickname = nickname;
        this.birth = birth;
        this.address = address;
        this.point = point;
        this.status = true; // 회원 가입 시 회원 상태 true 고정

        this.myBackgrounds = Objects.nonNull(myBackgrounds) ? myBackgrounds : new ArrayList<>();
        this.myMasks = Objects.nonNull(myMasks) ? myMasks : new ArrayList<>();
    }
}
