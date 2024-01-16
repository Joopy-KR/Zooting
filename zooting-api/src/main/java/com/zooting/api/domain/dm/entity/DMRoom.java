package com.zooting.api.domain.dm.entity;

import com.zooting.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "sender", referencedColumnName = "email")
    private Member fromMember;
    @ManyToOne
    @JoinColumn(name = "receiver", referencedColumnName = "email")
    private Member toMember;

    @Builder
    public DMRoom(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
    }
}
