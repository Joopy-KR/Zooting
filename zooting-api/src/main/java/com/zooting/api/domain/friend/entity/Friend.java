package com.zooting.api.domain.friend.entity;
import com.zooting.api.domain.member.entity.Member;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="friend_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="email")
    private Member follower;
    @ManyToOne
    @JoinColumn(name="email")
    private Member following;

    @Builder
    public Friend(Member follower, Member following) {
        this.follower = follower;
        this.following = following;
    }
}
