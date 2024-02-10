package com.zooting.api.domain.meeting.application;

import com.zooting.api.domain.meeting.dto.MeetingMemberDto;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

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

    LocalDateTime createdTime;

    Set<MeetingMemberDto> meetingMembers;

    @TimeToLive
    private Long expirationSeconds;
}
