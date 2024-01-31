package com.zooting.api.global.redis.service;

import com.zooting.api.global.redis.dto.RedisWaitRoom;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {
    private RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, RedisWaitRoom waitRoom){
        redisTemplate.convertAndSend(topic.getTopic(), waitRoom);
    }
}
