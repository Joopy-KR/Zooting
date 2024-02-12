package com.zooting.api.domain.meeting.application;

import com.zooting.api.domain.meeting.dto.MeetingMemberDto;
import org.springframework.stereotype.Component;

@Component
public class MatchingAlgorithm {
    private final int MEETING_CAPACITY = 4;
    private final int GENDER_CAPACITY = MEETING_CAPACITY / 2;

    public boolean isUnderMeetingCapacity(WaitingRoom waitingRoom){
        return waitingRoom.getMeetingMembers().size() < MEETING_CAPACITY;
    }
    public boolean catPassGenderLimit(WaitingRoom waitingRoom, MeetingMemberDto meetingMemberDto) {
        int manCount = 0;
        int womanCount = 0;

        for (MeetingMemberDto meetingMember : waitingRoom.getMeetingMembers()) {
            if (meetingMember.getGender().equals("man")) {
                manCount += 1;
            } else if (meetingMember.getGender().equals("woman")) {
                womanCount += 1;
            }
        }

        if(meetingMemberDto.getGender().equals("man") && manCount < GENDER_CAPACITY){
            return true;
        } else if (meetingMemberDto.getGender().equals("woman") && womanCount < GENDER_CAPACITY){
            return true;
        }
        return false;
    }
}
