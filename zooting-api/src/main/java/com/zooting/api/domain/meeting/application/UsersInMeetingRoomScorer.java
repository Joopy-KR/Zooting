package com.zooting.api.domain.meeting.application;
import com.zooting.api.domain.member.entity.Member;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsersInMeetingRoomScorer {
    Member me;
    double sumOfScore(List<Member> memberDtoList) {
        double sum = 0;
        for (Member roomMember : memberDtoList) {
            sum += scoringRoomMember(roomMember);
            sum += scoringMe(roomMember);
        }
        return sum;
    }

    double scoringRoomMember(Member member){
        // logic
        return member.getPoint();
    }

    double scoringMe(Member member){
        // logic
        return member.getPoint();
    }
}