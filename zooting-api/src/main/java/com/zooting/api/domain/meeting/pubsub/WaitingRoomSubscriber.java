package com.zooting.api.domain.meeting.pubsub;

import com.zooting.api.domain.meeting.application.WaitingRoom;
import com.zooting.api.domain.meeting.dao.WaitingRoomRedisRepository;
import com.zooting.api.domain.meeting.dto.MeetingMemberDto;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import io.openvidu.java.client.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Log4j2
@Component
@RequiredArgsConstructor
public class WaitingRoomSubscriber implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListener;
    private final WaitingRoomRedisRepository waitingRoomRedisRepository;
    private final SimpMessageSendingOperations webSocketTemplate;
    private final OpenVidu openVidu;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        final int MEETING_CAPACITY = 2;
        final WaitingRoomMessageDto parsedMessage = waitingRoomMessageParser(message);
        final String waitingRoomId = parsedMessage.getId();
        final String type = parsedMessage.getType();
        final int count = parsedMessage.getCount();

        log.info("[onMessage] Type: {}, WaitingRoomId: {}, Count: {}", type, waitingRoomId, count);

        WaitingRoom waitingRoom = waitingRoomRedisRepository.findById(waitingRoomId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));

        if (MessageType.REGISTER.getPrefix().contains(type) && count == MEETING_CAPACITY) {
            log.info("[onMessage] 유저 {}명이 대기열 등록을 완료했습니다. ", MEETING_CAPACITY);
            sendAcceptMessageToClient(waitingRoom);

            // 매칭 완료되었울 경우 10초내로 수락 버튼 누르도록
            waitingRoom.setExpirationSeconds(10L);
            waitingRoomRedisRepository.save(waitingRoom);
        } else if (MessageType.ACCEPTANCE.getPrefix().contains(type) && count == MEETING_CAPACITY) {
            log.info("[onMessage] 유저 {}명이 수락 버튼을 눌렀습니다. ", MEETING_CAPACITY);
            sendOpenViduTokenToClient(waitingRoom);
        }
    }

    /**
     * 꽉 찬 대기실 유저들에게 매칭 수락 버튼 발송
     *
     * @param waitingRoom 꽉 찬 대기실
     */

    private void sendAcceptMessageToClient(WaitingRoom waitingRoom) {
        log.info("[onMessage] key: {}, 매칭성공", waitingRoom.getWaitingRoomId());
        for (MeetingMemberDto meetingMemberDto : waitingRoom.getMeetingMembers()) {
            String email = meetingMemberDto.getEmail();
            RedisMatchRes redisMatchRes = new RedisMatchRes("match", waitingRoom.getWaitingRoomId());
            log.info("[onMessage] email: {} {} {}", email, redisMatchRes.type(), redisMatchRes.roomId());
            webSocketTemplate.convertAndSend("/api/sub/dm/" + email, redisMatchRes);
        }
    }

    /**
     * 유저 전원이 수락버튼을 눌렀을 경우 토큰 발급 후 대기실 삭제
     *
     * @param waitingRoom 매칭이 완료된 대기실
     */
    private void sendOpenViduTokenToClient(WaitingRoom waitingRoom) {
        try {
            Session session = openVidu.createSession();
            log.info("세션을 만들었습니다.");
            for (MeetingMemberDto meetingMemberDto : waitingRoom.getMeetingMembers()) {
                String email = meetingMemberDto.getEmail();
                Connection connection = session.createConnection();

                OpenviduTokenRes openviduTokenRes = new OpenviduTokenRes("openviduToken", connection.getToken());
                webSocketTemplate.convertAndSend("/api/sub/dm/" + email, openviduTokenRes);
            }
            waitingRoomRedisRepository.deleteById(waitingRoom.getWaitingRoomId());
            redisMessageListener.removeMessageListener(this, new ChannelTopic(waitingRoom.getWaitingRoomId()));
        } catch (OpenViduJavaClientException | OpenViduHttpException ex) {
            throw new RuntimeException(ex);
        }
    }

    private WaitingRoomMessageDto waitingRoomMessageParser(Message message) {
        String waitingRoomIdWithRedisHash = new String(message.getChannel());
        String waitingRoomId = waitingRoomIdWithRedisHash.split(":")[1];

        String[] typeAndCount = Objects.requireNonNull(
                redisTemplate.getStringSerializer().deserialize(message.getBody())).replaceAll("\"", "").split(" ");

        return WaitingRoomMessageDto.builder().id(waitingRoomId).type(typeAndCount[0])
                .count(Integer.parseInt(typeAndCount[1])).build();
    }
}