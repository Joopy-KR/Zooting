package com.zooting.api.domain.dm.api;

import com.zooting.api.domain.dm.application.DMService;
import com.zooting.api.domain.dm.dto.DMDto;
import com.zooting.api.domain.dm.entity.DMRoom;
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
//    private final RedisTemplate<String, Object> redisTemplate;
    private final DMService dmService;

    /**
     * 소켓을 통해 메시지가 들어오면 받아서 해당되는 채널로 전달
     */
    @MessageMapping("/chat/message")
    public void receiveAndSendMessage(DMDto dmDto, SimpMessageHeaderAccessor headerAccessor) {
        log.info("SEND_CHAT_SUCCESS (201 CREATED) ::");
        //ToDo : dmRoom Id 찾아오는 로직 변경 필요 채팅 칠때마다 쿼리날리는중,,
        DMRoom dmRoom = dmService.getDMRoom(dmDto.sender(), dmDto.receiver());
        dmService.saveDM(dmRoom, dmDto); // 만약 save 가 실패하면?
        template.convertAndSend("/sub/dm/"+dmDto.receiver(), dmDto);
    }
}