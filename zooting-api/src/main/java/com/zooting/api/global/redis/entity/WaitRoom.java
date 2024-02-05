package com.zooting.api.global.redis.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RedisHash("room:")
public class WaitRoom {
    @Id
    private UUID id;
    private Instant timestamp;
    private List<RedisMember> redisMember;

}
