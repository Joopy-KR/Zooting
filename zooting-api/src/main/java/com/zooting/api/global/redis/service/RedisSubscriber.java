package com.zooting.api.global.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.openvidu.java.client.Connection;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListener;
    private final SimpMessageSendingOperations webSocketTemplate;
    private final OpenVidu openVidu;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String key = new String(message.getChannel());
            String body = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            if(body.equals("accept")){
                log.info("[onMessage] key: {}, body: {} accept", key, body);
                redisTemplate.opsForList().set(key, 0, (Long)redisTemplate.opsForList().index(key, 0) + 1);
                if((Long)redisTemplate.opsForList().index(key, 0) == 4){
                    Session session = openVidu.createSession();
                    for (int i = 1; i < 5; i++) {
                        String email = (String) redisTemplate.opsForList().index(key, i);
                        Connection connection = session.createConnection();
                        webSocketTemplate.convertAndSend("/api/sub/dm/" + email, connection);
                        redisTemplate.delete(email);
                    }
                }
                return;
            }
            log.info("[onMessage] key: {}", key);
            Long listSize = redisTemplate.opsForList().size(key);
            if (listSize >= 5) { // 매칭완료 조건
                /*매칭이 완료되면 OpenVidu Session Create*/
                Session session = openVidu.createSession();
                log.info("[onMessage] key: {}, listSize: {} 매칭성공", key, listSize);
                for (int i = 0; i < 4; i++) {
                    String email = (String) redisTemplate.opsForList().rightPop(key);
                    log.info("[onMessage] email: {}", email);
                    Connection connection = session.createConnection();
                    log.info("[onMessage] connection: {}, token: {}", connection, connection.getToken());
                    //send token
                    webSocketTemplate.convertAndSend("/api/sub/dm/" + email, connection);
                    //delete matched users
                    redisTemplate.delete(email);
                }
                //destroy room
                deleteRoom(key);
            }
//            String body = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
//            RedisWaitRoom waitRoom = objectMapper.readValue(body, RedisWaitRoom.class);
            log.info("[onMessage] body: {}", body);
            log.info("[onMessage] key: {}, listSize: {}", key, listSize);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteRoom(String key) {
        ChannelTopic channelTopic = new ChannelTopic(key);
        redisMessageListener.removeMessageListener(this, channelTopic);
        redisTemplate.delete(key);
    }
}
