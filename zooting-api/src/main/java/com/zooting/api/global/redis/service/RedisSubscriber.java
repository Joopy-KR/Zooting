package com.zooting.api.global.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooting.api.global.redis.dto.RedisWaitRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String key = new String(message.getChannel());
            log.info("[onMessage] key: {}", key);
            Long listSize = redisTemplate.opsForList().size(key);
            if (listSize >= 4) {
                log.info("[onMessage] key: {}, listSize: {} 매칭성공", key, listSize);
                //send token
                //destory room
                deleteKeys(key, message);
            }
            String body = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
//            RedisWaitRoom waitRoom = objectMapper.readValue(body, RedisWaitRoom.class);
            log.info("[onMessage] body: {}", body);
            log.info("[onMessage] key: {}, listSize: {}", key, listSize);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteKeys(String key, Message message){
        for(int i = 0; i < 4; i++){
            String email = (String) redisTemplate.opsForList().leftPop(key);
            redisTemplate.delete(email);
        }
        redisTemplate.delete(key);
        //remove listener

    }
}
