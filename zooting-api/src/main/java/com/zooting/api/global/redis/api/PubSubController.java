package com.zooting.api.global.redis.api;

import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.common.code.SuccessCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import com.zooting.api.global.redis.service.RedisPubSubService;
import com.zooting.api.global.redis.service.RedisPublisher;
import com.zooting.api.global.redis.service.RedisSubscriber;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
    private final RedisPubSubService redisPubSubService;

    //    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberRepository memberRepository;

    @Operation(summary = "대기방 조회", description = "대기방 조회")
    // 유효한 Topic 리스트 반환
    @GetMapping("/room")
    public Set<String> findAllRoom() {
        return redisTemplate.keys("room:*");
    }

//    @GetMapping("/matching/start")
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

    @Operation(summary = "대기방 찾기", description = "대기방 찾기")
    /*TEST 입장*/
    @GetMapping("/matching/start")
    public ResponseEntity<BaseResponse<String>> startMatching(@RequestParam String loginEmail) {
        String waitRoom = redisPubSubService.startMatching(loginEmail);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                waitRoom
        );
    }

//    @Operation(summary = "대기방 생성", description = "대기방 생성")
//    // 신규 Topic을 생성하고 Listener등록
//    @PutMapping("/room")
//    public void createRoom(String loginEmail) {
//        UUID randomUUID = UUID.randomUUID();
//        ChannelTopic channel = new ChannelTopic("room:" + randomUUID);
//        redisMessageListener.addMessageListener(redisSubscriber, channel);
//        redisTemplate.opsForList().rightPush("room:" + randomUUID, loginEmail);
//        redisTemplate.opsForValue().set(loginEmail, "room:" + randomUUID);
//        redisPublisher.publish(channel.getTopic(), randomUUID.toString());
//    }

//    @Operation(summary = "메세지전송", description = "메세지전송")
//    // 특정 Topic에 메시지 발행
//    @PostMapping("/room/{roomId}")
//    public void pushMessage(@RequestParam String message, @PathVariable String roomId) {
//        redisPublisher.publish(roomId, message);
//    }

    @Operation(summary = "매칭 취소", description = "매칭취소")
    // Topic 삭제 후 Listener 해제, Topic Map에서 삭제
    @DeleteMapping("/matching/cancel")
    public ResponseEntity<BaseResponse<String>> cancelMatching(@AuthenticationPrincipal UserDetails userDetails) {
        String roomId = (String) redisTemplate.opsForValue().get(userDetails.getUsername());
        if (roomId == null) {
            throw new BaseExceptionHandler(ErrorCode.NOT_MATCHING);
        }
        redisTemplate.delete(userDetails.getUsername());
        redisTemplate.opsForList().remove(roomId, 1, userDetails.getUsername());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "매칭 취소 성공"
        );
    }
}