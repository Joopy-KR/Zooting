package com.zooting.api.domain.disabled.entity;

import com.zooting.api.domain.BaseEntity;
import com.zooting.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "disabled_member")
public class DisabledMember extends BaseEntity {
    @Id
    @Column(name = "disabled_member_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "email")
    private Member member;
    @Column(name = "start_date")
    private String startDate;
    @Column(name = "end_date")
    private String endDate;

    @Builder
    public DisabledMember(Member member, String startDate, String endDate) {
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
