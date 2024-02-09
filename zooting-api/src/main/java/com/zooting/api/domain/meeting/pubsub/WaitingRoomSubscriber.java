package com.zooting.api.domain.meeting.pubsub;

import com.zooting.api.domain.meeting.dao.WaitingRoomRedisRepository;
import com.zooting.api.domain.meeting.dto.MeetingMemberDto;
import com.zooting.api.domain.meeting.redisdto.WaitingRoom;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import io.openvidu.java.client.Connection;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Session;
import java.util.Objects;
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
        final int MEETING_CAPACITY = 1;
        final WaitingRoomMessageDto parsedMessage = waitingRoomMessageParser(message);
        final String waitingRoomId = parsedMessage.getId();
        final String type = parsedMessage.getType();
        final int count = parsedMessage.getCount();

        WaitingRoom waitingRoom = waitingRoomRedisRepository.findById(waitingRoomId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));

        if (MessageType.REGISTER.getPrefix().contains(type) && count == MEETING_CAPACITY) {
            sendAcceptMessageToClient(waitingRoom, count);
        } else if (MessageType.ACCEPTANCE.getPrefix().contains(type) && count == MEETING_CAPACITY) {
            sendOpenviduTokenToClient(waitingRoom, count);
        }
    }

    private void sendAcceptMessageToClient(WaitingRoom waitingRoom, int count) {
        log.info("[onMessage] key: {}, register: {} 매칭성공", waitingRoom.getWaitingRoomId(), count);
        for (MeetingMemberDto meetingMemberDto : waitingRoom.getMeetingMembers()) {
            String email = meetingMemberDto.getEmail();
            RedisMatchRes redisMatchRes = new RedisMatchRes("match", waitingRoom.getWaitingRoomId());
            log.info("[onMessage] email: {} {} {}", email, redisMatchRes.type(), redisMatchRes.roomId());
            webSocketTemplate.convertAndSend("/api/sub/dm/" + email, redisMatchRes);
        }
    }

    private void sendOpenviduTokenToClient(WaitingRoom waitingRoom, int count) {
        try {
            Session session = openVidu.createSession();
            log.info("세션을 만들었습니다. 인원 : {}", count);
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