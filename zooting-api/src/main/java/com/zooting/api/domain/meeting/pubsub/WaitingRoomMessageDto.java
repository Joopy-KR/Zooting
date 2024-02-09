package com.zooting.api.domain.meeting.pubsub;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WaitingRoomMessageDto {
    private String id;
    private String type;
    private int count;
}
