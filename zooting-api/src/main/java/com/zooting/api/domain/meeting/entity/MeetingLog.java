package com.zooting.api.domain.meeting.entity;
import com.zooting.api.domain.member.entity.Member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class MeetingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="meeting_log_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_email", referencedColumnName = "email")
    private Member member;

    @OneToOne(mappedBy = "meetingId")
    private MeetingParticipants meetingParticipants;

    @Builder
    public MeetingLog(Member member) {
        this.member = member;
    }
}
