package com.zooting.api.global.redis.dto;

import lombok.Data;

@Data
public class RedisWaitRoom {
    private String roomId;
    private String message;
}