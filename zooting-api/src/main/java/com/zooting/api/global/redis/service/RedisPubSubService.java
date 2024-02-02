package com.zooting.api.global.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class RedisPubSubService {
    // topic에 메시지 발행을 기다리는 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    // 발행자
    private final RedisPublisher redisPublisher;
    // 구독자
    private final RedisSubscriber redisSubscriber;

    private final RedisTemplate<String, Object> redisTemplate;

    String startMatching(String loginEmail) {
        Set<String> waitRooms = redisTemplate.keys("room:*");
        /* 매칭 로직 */
        for (String waitRoom : waitRooms) {
            Long listSize = redisTemplate.opsForList().size(waitRoom);
            if (listSize < 4) {
                log.info("findRoom: {}", waitRoom);
                redisTemplate.opsForList().rightPush(waitRoom, loginEmail);
                redisTemplate.opsForValue().set(loginEmail, waitRoom);
                redisPublisher.publish(waitRoom, loginEmail + "님이 입장하셨습니다.");
                return waitRoom;
            }
        }
        /* 매칭 실패시 */
        createRoom(loginEmail);
        return "startMatching";
    }

    public void createRoom(String loginEmail) {
        UUID randomUUID = UUID.randomUUID();
        ChannelTopic channel = new ChannelTopic("room:" + randomUUID);
        redisMessageListener.addMessageListener(redisSubscriber, channel);
        redisTemplate.opsForList().rightPush("room:" + randomUUID, loginEmail);
        redisTemplate.opsForValue().set(loginEmail, "room:" + randomUUID);
        redisPublisher.publish(channel.getTopic(), randomUUID.toString());
    }
}
