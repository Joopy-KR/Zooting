package com.zooting.api.domain.disabled.entity;

import com.zooting.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DisabledUser {
    @Id
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "email")
    private Member member;
    @Column(name = "start_date")
    private String startDate;
    @Column(name = "end_date")
    private String endDate;

    @Builder
    public DisabledUser(Member member, String startDate, String endDate) {
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
