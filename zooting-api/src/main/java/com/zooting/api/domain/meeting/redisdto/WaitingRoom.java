package com.zooting.api.domain.meeting.redisdto;

import com.zooting.api.domain.meeting.dto.MeetingMemberDto;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "room")
public class WaitingRoom {

    @Id
    String waitingRoomId;
    int acceptCount;
    Set<MeetingMemberDto> meetingMembers;
}
