package com.zooting.api.domain.dm.api;

import com.zooting.api.domain.dm.dto.DMDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DMWebSocketController {

    private final SimpMessageSendingOperations template;
    private final RedisTemplate<String, Object> redisTemplate;
//    private final DMWebSocketService dmWebSocketService;

    /**
     * 소켓을 통해 메시지가 들어오면 받아서 해당되는 채널로 전달
     */
    @MessageMapping("/chat/sendMessage")
    public void receiveAndSendMessage(DMDto dmDto, SimpMessageHeaderAccessor headerAccessor) {
        log.info("SEND_CHAT_SUCCESS (201 CREATED) ::");
        template.convertAndSend("/sub/chat/a", dmDto);
        redisTemplate.convertAndSend("/sub/chat", dmDto);
    }
}