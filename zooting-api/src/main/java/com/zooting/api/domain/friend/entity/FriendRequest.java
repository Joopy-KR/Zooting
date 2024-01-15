package com.zooting.api.domain.friend.entity;
import com.zooting.api.domain.member.entity.Member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="friend_request_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="request_from")
    private Member from;
    @ManyToOne
    @JoinColumn(name="request_to")
    private Member to;

    @Builder
    public FriendRequest(Member from, Member to) {
        this.from = from;
        this.to = to;
    }
}
