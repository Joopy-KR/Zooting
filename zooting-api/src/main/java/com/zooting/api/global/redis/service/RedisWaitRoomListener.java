package com.zooting.api.global.redis.service;

import com.zooting.api.global.redis.dto.RedisWaitRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class RedisWaitRoomListener implements MessageListener {
    private final RedisTemplate<String, RedisWaitRoom> redisTemplate;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String roomId = new String(message.getChannel());
        RedisWaitRoom waitRoom = (RedisWaitRoom) redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.info("[onMessage] roomId: {}, message: {}", roomId, waitRoom);
        redisTemplate.convertAndSend("/redis/sub/" + roomId, waitRoom);
    }
}
