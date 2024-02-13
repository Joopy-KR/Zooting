package com.zooting.api.domain.dm.api;

import com.zooting.api.domain.dm.application.DMService;
import com.zooting.api.domain.dm.dto.request.DMReq;
import com.zooting.api.global.common.SocketBaseDtoRes;
import com.zooting.api.global.common.SocketType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "DM 웹소켓", description = "DM웹소켓 관련 API")
public class DMWebSocketController {

    private final SimpMessageSendingOperations template;
    private final RedisTemplate<String, Object> redisTemplate;
    private final DMService dmService;

    /**
     * 소켓을 통해 메시지가 들어오면 받아서 해당되는 채널로 전달
     */
    @Operation(summary = "DM 메시지 전송")
    @MessageMapping("/dm/message")
    public void receiveAndSendMessage(DMReq dmReq, SimpMessageHeaderAccessor headerAccessor) {
        log.info("SEND_CHAT_SUCCESS (201 CREATED) ::");
        dmService.saveDM(dmReq);
        template.convertAndSend("/api/sub/" + dmReq.receiver(), new SocketBaseDtoRes<>(SocketType.MESSAGE, dmReq));
    }
}