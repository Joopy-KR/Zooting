package com.zooting.api.domain.meeting.redisdto;

import com.zooting.api.domain.meeting.dto.MeetingMemberDto;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@Setter
@RedisHash(value = "room")
@RequiredArgsConstructor
@AllArgsConstructor
public class WaitingRoom {

    @Id
    String roomId;

    int acceptCount;

    Set<MeetingMemberDto> meetingMembers;
}
