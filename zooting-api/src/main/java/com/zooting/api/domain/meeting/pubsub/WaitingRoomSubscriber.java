package com.zooting.api.domain.meeting.pubsub;

import com.zooting.api.domain.meeting.dao.WaitingRoomRedisRepository;
import com.zooting.api.domain.meeting.dto.MeetingMemberDto;
import com.zooting.api.domain.meeting.redisdto.WaitingRoom;
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
        WaitingRoomMessageDto parsedMessage = waitingRoomMessageParser(message);
        String waitingRoomId = parsedMessage.getId();
        String type = parsedMessage.getType();
        int count = parsedMessage.getCount();

        if (MessageType.REGISTER.getPrefix().equals(type)) {
            sendAcceptMessageToClient(waitingRoomId, type, count);
        } else if (MessageType.ACCEPTANCE.getPrefix().equals(type)) {
            sendOpenviduTokenToClient(waitingRoomId, count);
        }
    }

    private void sendAcceptMessageToClient(String waitingRoomId, String type, int count){
        WaitingRoom waitingRoom = waitingRoomRedisRepository.findById(waitingRoomId).orElse(null);
        log.info("[onMessage] key: {}", type);
        if (waitingRoom != null && count == 4) { // 매칭완료 조건
            log.info("[onMessage] key: {}, register: {} 매칭성공", waitingRoomId, count);
            for (MeetingMemberDto meetingMemberDto : waitingRoom.getMeetingMembers()) {
                String email = meetingMemberDto.getEmail();
                RedisMatchRes redisMatchRes = new RedisMatchRes("match", waitingRoomId);
                log.info("[onMessage] email: {} {} {}", email, redisMatchRes.type(), redisMatchRes.roomId());
                webSocketTemplate.convertAndSend("/api/sub/dm/" + email, redisMatchRes);
            }
        }
    }
    private void sendOpenviduTokenToClient(String waitingRoomId, int count){
        WaitingRoom waitingRoom = waitingRoomRedisRepository.findById(waitingRoomId).orElse(null);
        log.info("[onMessage] key: {}, body: {}", waitingRoomId, count);
        if (waitingRoom != null && count == 4) {
            try {
                Session session = openVidu.createSession();
                for (MeetingMemberDto meetingMemberDto : waitingRoom.getMeetingMembers()) {
                    String email = meetingMemberDto.getEmail();
                    Connection connection = session.createConnection();

                    OpenviduTokenRes openviduTokenRes = new OpenviduTokenRes("openviduToken",
                            connection.getToken());
                    webSocketTemplate.convertAndSend("/api/sub/dm/" + email, openviduTokenRes);
                }
                waitingRoomRedisRepository.deleteById(waitingRoomId);
                redisMessageListener.removeMessageListener(this, new ChannelTopic(waitingRoomId));
            } catch (OpenViduJavaClientException | OpenViduHttpException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private WaitingRoomMessageDto waitingRoomMessageParser(Message message){
        String id = new String(message.getChannel());
        log.info("채널(방 제목): {}", id);
        String[] typeAndCount = Objects.requireNonNull(
                redisTemplate.getStringSerializer().deserialize(message.getBody())).replaceAll("\"", "").split(" ");

        log.info("명령어: {}", typeAndCount[0]);
        log.info("값: {}", typeAndCount[1]);

        return WaitingRoomMessageDto.builder()
                .id(id)
                .type(typeAndCount[0])
                .count(Integer.parseInt(typeAndCount[1]))
                .build();
    }
}