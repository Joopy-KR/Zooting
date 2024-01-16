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
public class DMRoom {
    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender")
    private Member sender;
    @ManyToOne
    @JoinColumn(name = "receiver")
    private Member receiver;
    @OneToMany
    private List<DM> dms = new ArrayList<>();
    @Builder
    public DMRoom(Member sender, Member receiver, List<DM> dms) {
        this.sender = sender;
        this.receiver = receiver;
        this.dms = Objects.nonNull(dms) ? dms : new ArrayList<>();
    }
}
