package com.zooting.api.global.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooting.api.global.redis.dto.response.OpenviduTokenRes;
import com.zooting.api.global.redis.dto.response.RedisMatchRes;
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
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListener;
    private final SimpMessageSendingOperations webSocketTemplate;
    private final OpenVidu openVidu;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String key = new String(message.getChannel());
            String body = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            log.info("body {} equals accept {}", body, body.contains("accept"));
            if(body.contains("accept")){
                log.info("[onMessage] key: {}, body: {}", key, body);
                Long currentAccept = Long.valueOf((String) redisTemplate.opsForList().index(key, 0));
                redisTemplate.opsForList().set(key, 0, currentAccept + 1);
                if(Long.valueOf((String)redisTemplate.opsForList().index(key, 0)) == 4){
                    Session session = openVidu.createSession();
                    for (int i = 1; i < 5; i++) {
                        String email = (String) redisTemplate.opsForList().rightPop(key);
                        Connection connection = session.createConnection();
                        OpenviduTokenRes openviduTokenRes = new OpenviduTokenRes("openviduToken", connection.getToken());
                        webSocketTemplate.convertAndSend("/api/sub/dm/" + email, openviduTokenRes);
                        redisTemplate.delete(email);
                    }
                    //destroy room
                    deleteRoom(key);
                }
                return;
            }

    public void deleteRoom(String key) {
        ChannelTopic channelTopic = new ChannelTopic(key);
        redisMessageListener.removeMessageListener(this, channelTopic);
        redisTemplate.delete(key);
    }
}
