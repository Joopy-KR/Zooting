package com.zooting.api.domain.dm.entity;

import com.zooting.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

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
    private Member fromMember;
    @ManyToOne
    @JoinColumn(name = "receiver")
    private Member toMember;

    @Builder
    public DMRoom(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
    }
}
