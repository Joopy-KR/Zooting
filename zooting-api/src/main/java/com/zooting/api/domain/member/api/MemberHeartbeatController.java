package com.zooting.api.domain.member.api;

import com.zooting.api.domain.member.application.MemberHeartbeatService;
import com.zooting.api.domain.member.dto.request.HeartBeatReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@Tag(name = "멤버 접속 상태", description = "멤버 접속 상태 Socket API")
public class MemberHeartbeatController {
    private final String JOB_NAME = "CHECK_HEARTBEAT";
    private final SimpMessageSendingOperations template;
    private final MemberHeartbeatService memberHeartbeatService;

    @Operation(summary = "Heartbeat 메시지 수신")
    @MessageMapping("/member/heartbeat")
    public void memberHeartbeatCheck(HeartBeatReq request) {

    }

    @Scheduled(cron = "0 0/2 * * * ?")
    @Bean(JOB_NAME)
    public void checkAllMemberOnline() {

    }
}
