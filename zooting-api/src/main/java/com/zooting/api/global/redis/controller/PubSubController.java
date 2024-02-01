package com.zooting.api.global.redis.controller;

import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import com.zooting.api.global.redis.dto.RedisWaitRoom;
import com.zooting.api.global.redis.service.RedisPublisher;
import com.zooting.api.global.redis.service.RedisSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/match")
@RestController
public class PubSubController {
    // topic에 메시지 발행을 기다리는 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    // 발행자
    private final RedisPublisher redisPublisher;
    // 구독자
    private final RedisSubscriber redisSubscriber;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private MemberRepository memberRepository;

    // 유효한 Topic 리스트 반환
    @GetMapping("/room")
    public Set<String> findAllRoom() {
        return redisTemplate.keys("room:*");
    }

//    @GetMapping("/room/enter")
//    public String findRoom(@AuthenticationPrincipal UserDetails userDetails) {
////        if(redisTemplate.hasKey(userDetails.getUsername())){
////            return (String) redisTemplate.opsForValue().get(userDetails.getUsername());
////        }
//        Set<String> waitRooms = redisTemplate.keys("room:*");k
//        /* 매칭 로직 */
//        for (String waitRoom : waitRooms) {
//            Long listSize = redisTemplate.opsForList().size(waitRoom);
//            if (listSize < 4) {
//                log.info("findRoom: {}", waitRoom);
//                redisTemplate.opsForList().rightPush(waitRoom, userDetails.getUsername());
//                redisTemplate.opsForValue().set(userDetails.getUsername(), waitRoom);
//                redisPublisher.publish(waitRoom, userDetails.getUsername() + "님이 입장하셨습니다.");
//                return waitRoom;
//            }
//        }
//        /* 매칭 실패시 */
//        createRoom(userDetails.getUsername());
//        return null;
//    }

    /*TEST 입장*/
    @GetMapping("/room/enter")
    public String findRoom(@RequestParam String loginEmail) {
//        if(redisTemplate.hasKey(userDetails.getUsername())){
//            return (String) redisTemplate.opsForValue().get(userDetails.getUsername());
//        }
        Set<String> waitRooms = redisTemplate.keys("room:*");
        /* 매칭 로직 */
        for (String waitRoom : waitRooms) {
            Long listSize = redisTemplate.opsForList().size(waitRoom);
            if (listSize < 4) {
                log.info("findRoom: {}", waitRoom);
                redisTemplate.opsForList().rightPush(waitRoom, loginEmail);
                redisTemplate.opsForValue().set(loginEmail, waitRoom);
                redisPublisher.publish(waitRoom, loginEmail + "님이 입장하셨습니다.");
                return waitRoom;
            }
        }
        /* 매칭 실패시 */
        createRoom(loginEmail);
        return null;
    }
    // 신규 Topic을 생성하고 Listener등록
    @PutMapping("/room")
    public void createRoom(String loginEmail) {
        UUID randomUUID = UUID.randomUUID();
        ChannelTopic channel = new ChannelTopic("room:" + randomUUID);
        redisMessageListener.addMessageListener(redisSubscriber, channel);
        redisTemplate.opsForList().rightPush("room:" + randomUUID, loginEmail);
        redisTemplate.opsForValue().set(loginEmail, "room:" + randomUUID);
        redisPublisher.publish(channel.getTopic(), randomUUID.toString());
    }
    // 특정 Topic에 메시지 발행
    @PostMapping("/room/{roomId}")
    public void pushMessage(@RequestParam String message, @PathVariable String roomId) {
        redisPublisher.publish(roomId, message);
    }

    // Topic 삭제 후 Listener 해제, Topic Map에서 삭제
    @DeleteMapping("/room/{roomId}")
    public void cancelMatching(@PathVariable String roomId, @AuthenticationPrincipal UserDetails userDetails) {
        redisTemplate.delete(userDetails.getUsername());
        redisTemplate.opsForList().remove(roomId, 1, userDetails.getUsername());
    }
}