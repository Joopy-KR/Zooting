package com.zooting.api.global.redis.service;

import com.zooting.api.global.redis.dto.RedisWaitRoom;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {
    private RedisTemplate<String, Object> redisTemplate;

    public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void publish(String channel, String message){
        redisTemplate.convertAndSend(channel, message);
    }
}
