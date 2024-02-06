package com.zooting.api.global.redis.service;

import com.zooting.api.domain.meeting.pubsub.RedisPublisher;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.redis.entity.RedisMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class RedisPubSubService {
    // topic에 메시지 발행을 기다리는 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    // 발행자
    private final RedisPublisher redisPublisher;
    // 구독자
    private final RedisSubscriber redisSubscriber;

    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberRepository memberRepository;

    public String startMatching(String loginEmail) {
        //email로 RedisMember 변환
        Member member = memberRepository.findByEmail(loginEmail).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        /* RedisMember 생성 */
        RedisMember redisMember = RedisMember.builder()
                .email(member.getEmail())
                .gender(member.getGender())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .personality(member.getAdditionalInfo().getPersonality())
                .animal(member.getAdditionalInfo().getAnimal())
                .introduce(member.getAdditionalInfo().getIntroduce())
                .interest(member.getAdditionalInfo().getInterest())
                .idealAnimal(member.getAdditionalInfo().getIdealAnimal())
                .animalFace(member.getAnimalFace())
                .blockFromList(member.getBlockFromList())
                .build();

        Set<String> waitRooms = redisTemplate.keys("room:*");
        /* 매칭 로직 */
        for (String waitRoom : waitRooms) {
            Long listSize = redisTemplate.opsForList().size(waitRoom);
            if (listSize < 5) {
                log.info("findRoom: {}", waitRoom);
                redisTemplate.opsForList().rightPush(waitRoom, loginEmail);
                redisTemplate.opsForValue().set(loginEmail, waitRoom);
                redisPublisher.publish(waitRoom, loginEmail + "님이 입장하셨습니다.");
                return waitRoom;
            }
        }
        /* 매칭 실패시 */
        return createRoom(loginEmail);
    }

    public String createRoom(String loginEmail) {
        UUID randomUUID = UUID.randomUUID();
        ChannelTopic channel = new ChannelTopic("room:" + randomUUID);
        redisMessageListener.addMessageListener(redisSubscriber, channel);
        redisTemplate.opsForList().rightPush("room:" + randomUUID, 0);
        redisTemplate.opsForList().rightPush("room:" + randomUUID, loginEmail);
        redisTemplate.opsForValue().set(loginEmail, "room:" + randomUUID);
        redisPublisher.publish(channel.getTopic(), randomUUID.toString());
        return "room:" + randomUUID;
    }

    public void acceptMatching(String channel) {
        redisPublisher.publish(channel, "accept");
    }
}
