package com.zooting.api.domain.meeting.entity;
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
public class MeetingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="meetinglog_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="participant1", referencedColumnName = "email")
    private Member participant1;
    @ManyToOne
    @JoinColumn(name="participant2", referencedColumnName = "email")
    private Member participant2;
    @ManyToOne
    @JoinColumn(name="participant3", referencedColumnName = "email")
    private Member participant3;
    @ManyToOne
    @JoinColumn(name="participant4", referencedColumnName = "email")
    private Member participant4;

    @Builder
    public MeetingLog(Member participant1, Member participant2, Member participant3, Member participant4) {
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.participant3 = participant3;
        this.participant4 = participant4;
    }
}
