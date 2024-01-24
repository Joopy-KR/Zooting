package com.zooting.api.domain.dm.entity;

import com.zooting.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dm_room")
public class DMRoom extends BaseEntity {
    @Id
    @Column(name = "dm_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender")
    private Member sender;
    @ManyToOne
    @JoinColumn(name = "receiver")
    private Member receiver;
    @OneToMany(mappedBy = "dmRoom")
    private List<DM> dms;
    @Builder
    public DMRoom(Member sender, Member receiver, List<DM> dms) {
        this.sender = sender;
        this.receiver = receiver;
        this.dms = Objects.nonNull(dms) ? dms : new ArrayList<>();
    }
}
