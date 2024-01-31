package com.zooting.api.global.redis.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RedisWaitRoom implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roomId;
    private String email;
    private String message;
}