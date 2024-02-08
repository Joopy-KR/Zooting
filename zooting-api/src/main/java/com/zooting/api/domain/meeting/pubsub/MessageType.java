package com.zooting.api.domain.meeting.pubsub;

import lombok.Getter;

@Getter
public enum MessageType {
    REDIS_HASH("room:"),
    REGISTER("register "),
    ACCEPTANCE("acceptance ");

    private final String prefix;
    MessageType(String prefix){
        this.prefix = prefix;
    }
}
