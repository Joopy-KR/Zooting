package com.zooting.api.domain.dm.api;

import com.zooting.api.domain.dm.application.DMService;
import com.zooting.api.domain.dm.dto.request.DMReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DMWebSocketController {

    private final SimpMessageSendingOperations template;
//    private final RedisTemplate<String, Object> redisTemplate;
    private final DMService dmService;

    /**
     * 소켓을 통해 메시지가 들어오면 받아서 해당되는 채널로 전달
     */
    @MessageMapping("/dm/message")
    public void receiveAndSendMessage(DMReq dmReq, SimpMessageHeaderAccessor headerAccessor) {
        log.info("SEND_CHAT_SUCCESS (201 CREATED) ::");
        dmService.saveDM(dmReq);
        template.convertAndSend("api/sub/dm/"+ dmReq.receiver(), dmReq);
    }
}