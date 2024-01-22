package com.zooting.api.domain.report.entity;

import com.zooting.api.domain.BaseEntity;
import com.zooting.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report_list")
public class ReportList extends BaseEntity {

    @Id
    @Column(name = "report_list_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reason;
    private String date;
    @ManyToOne
    @JoinColumn(name = "email")
    private Member member;

    @Builder
    public ReportList(String reason, String date, Member member) {
        this.reason = reason;
        this.date = date;
        this.member = member;
    }
}
