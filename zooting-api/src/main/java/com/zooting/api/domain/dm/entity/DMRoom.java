package com.zooting.api.domain.dm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class DMRoom {
    @Id
    @Column(name = "dm_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender")
    @JsonIgnore //
    private Member sender;
    @ManyToOne
    @JoinColumn(name = "receiver")
    @JsonIgnore
    private Member receiver;
    @OneToMany(mappedBy = "dmRoom")
    private List<DM> dms = new ArrayList<>();
    @Builder
    public DMRoom(Member sender, Member receiver, List<DM> dms) {
        this.sender = sender;
        this.receiver = receiver;
        this.dms = Objects.nonNull(dms) ? dms : new ArrayList<>();
    }
}
