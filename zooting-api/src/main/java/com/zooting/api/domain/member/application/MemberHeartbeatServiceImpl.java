package com.zooting.api.domain.member.application;

import com.google.gson.Gson;
import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.member.dto.request.HeartBeatReq;
import com.zooting.api.domain.member.dto.response.HeartBeatRes;
import com.zooting.api.global.common.SocketBaseDtoRes;
import com.zooting.api.global.common.SocketType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberHeartbeatServiceImpl implements MemberHeartbeatService {
    private static final SocketType SOCKET_TYPE = SocketType.HEARTBEAT;
    private static final String HEARTBEAT_HASH = "heartbeat:";
    private static final Long TIME_TO_LIVE = 120L;
    private final FriendRepository friendRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson;

    @Transactional(readOnly = true)
    public SocketBaseDtoRes<List<HeartBeatRes>> updateHeartbeatStatus(HeartBeatReq heartBeatReq) {
        var online = Optional.ofNullable(redisTemplate.opsForValue().get(HEARTBEAT_HASH + heartBeatReq.memberId()));

        List<HeartBeatRes> heartBeatRes = new ArrayList<>();
        // 저장되어 있지 않거나 거의 만료 되었다면 접속한 다른 유저 정보를 호출
        if (online.isEmpty()) { // 저장되지 않은 경우
            checkFriendOnline(heartBeatReq, heartBeatRes);
        }
        if (online.isPresent()) {
            var prevTime = gson.fromJson((String) online.get(), LocalDateTime.class);
            if (Duration.between(LocalDateTime.now(), prevTime).getSeconds() > TIME_TO_LIVE * 2) {
                checkFriendOnline(heartBeatReq, heartBeatRes);
            }
        }

        // 접속 시간 갱신
        LocalDateTime now = LocalDateTime.now();
        redisTemplate.opsForValue().set(HEARTBEAT_HASH + heartBeatReq.memberId(), gson.toJson(now),
                Duration.ofMinutes(TIME_TO_LIVE * 3)); // 3ttl (6min)동안 생존

        return new SocketBaseDtoRes<>(SOCKET_TYPE, heartBeatRes);
    }

    private void checkFriendOnline(HeartBeatReq heartBeatReq, List<HeartBeatRes> heartBeatRes) {
        var friends = friendRepository.findFriendByFollower(heartBeatReq.memberId());
        for (var friend : friends) {
            var check = redisTemplate.opsForValue().get(HEARTBEAT_HASH + friend.getFollowing().getEmail());
            // 친구가 접속해 있는 경우
            if (Objects.nonNull(check)) {
                heartBeatRes.add(new HeartBeatRes(friend.getFollowing().getNickname(), true, gson.fromJson((String) check, LocalDateTime.class)));
            } else {
                heartBeatRes.add(new HeartBeatRes(friend.getFollowing().getNickname(), false, null));
            }
        }
    }

    public List<String> checkAllMemberOnline() {
        var memberIds = redisTemplate.keys(HEARTBEAT_HASH);
        if (Objects.isNull(memberIds) || memberIds.isEmpty()) return List.of();

        List<String> offLineMemberIds = new ArrayList<>();
        LocalDateTime curTime = LocalDateTime.now();
        for (var memberId : memberIds) {
            LocalDateTime accessTime = gson.fromJson((String) redisTemplate.opsForValue().get(HEARTBEAT_HASH + memberId), LocalDateTime.class);
            long diffSecond = Duration.between(curTime, accessTime).getSeconds();

            // 접속이 끊긴 유저 감지
            if (diffSecond > TIME_TO_LIVE * 2) {
                // 유저의 접속이 종료되면 접속이 끊어진 유저의 정보를 저장
                offLineMemberIds.add(memberId);
            }
        }

        return offLineMemberIds;
    }
}
