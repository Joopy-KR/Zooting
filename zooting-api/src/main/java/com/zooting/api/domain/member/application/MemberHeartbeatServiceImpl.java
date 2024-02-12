package com.zooting.api.domain.member.application;

import com.google.gson.Gson;
import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.member.dto.request.HeartBeatReq;
import com.zooting.api.domain.member.dto.response.AccessMemberStatus;
import com.zooting.api.domain.member.dto.response.HeartBeatRes;
import com.zooting.api.global.common.SocketBaseDtoRes;
import com.zooting.api.global.common.SocketType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public SocketBaseDtoRes<HeartBeatRes> loadOnlineFriends(HeartBeatReq heartBeatReq) {
        var online = redisTemplate.getExpire(HEARTBEAT_HASH + heartBeatReq.memberId(), TimeUnit.SECONDS);

        Set<String> onlineFriends;
        // 저장되어 있지 않거나 2ttl 이하이면 접속한 다른 유저 정보를 호출
        if (Objects.isNull(online) || online < 0) {
            onlineFriends = checkFriendOnline(heartBeatReq);
            addOnlineFriends(heartBeatReq, onlineFriends);
        } // 저장되어 있다면 저장된 데이터 호출하고 expire 증가
        else {
            onlineFriends = getOnlineFriends(heartBeatReq);
        }

        redisTemplate.expire(HEARTBEAT_HASH + heartBeatReq.memberId(), TIME_TO_LIVE * 3, TimeUnit.SECONDS);
        return new SocketBaseDtoRes<>(SOCKET_TYPE, new HeartBeatRes(onlineFriends.stream().toList()));
    }

    private void addOnlineFriends(HeartBeatReq heartBeatReq, Set<String> onlineFriends) {
        String key = HEARTBEAT_HASH + heartBeatReq.memberId();
        // 각 원소를 Set에 추가
        for (String friend : onlineFriends) {
            redisTemplate.opsForSet().add(key, friend);
        }
    }

    private Set<String> getOnlineFriends(HeartBeatReq heartBeatReq) {
        var result = redisTemplate.opsForSet().members(HEARTBEAT_HASH + heartBeatReq.memberId());
        if (Objects.isNull(result)) return Set.of();
        return result.stream().map(Object::toString).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<String> checkFriendOnline(HeartBeatReq heartBeatReq) {
        Set<String> onlineFriends = new HashSet<>();
        var friends = friendRepository.findFriendByFollower(heartBeatReq.memberId());
        for (var friend : friends) {
            var check = redisTemplate.getExpire(HEARTBEAT_HASH + friend.getFollowing());
            // 친구가 접속해 있는 경우
            if (Objects.nonNull(check) && check > TIME_TO_LIVE) {
                onlineFriends.add(friend.getFollowing().getNickname());
            }
        }
        return onlineFriends;
    }
    public void updateMemberStatus() {
        AccessMemberStatus accessMemberStatus = updateOfflineMemberStatus();
        if (Objects.isNull(accessMemberStatus)) return;

        updateOnlineMemberStatus(accessMemberStatus);
    }

    private void updateOnlineMemberStatus(AccessMemberStatus accessMemberStatus) {
        for (var onlineMember : accessMemberStatus.onlineMemberIds()) {

        }
    }

    private AccessMemberStatus updateOfflineMemberStatus() {
        var memberIds = redisTemplate.keys(HEARTBEAT_HASH); // 저장된 멤버의 키 로드
        if (Objects.isNull(memberIds) || memberIds.isEmpty()) return null;

        List<String> onlineMemberIds = new ArrayList<>();
        List<String> offlineMemberIds = new ArrayList<>();
        for (var memberId : memberIds) {
            var expireTime = redisTemplate.getExpire(HEARTBEAT_HASH + memberId);
            if (Objects.isNull(expireTime) || expireTime < TIME_TO_LIVE) {
                offlineMemberIds.add(memberId);
            } else {
                onlineMemberIds.add(memberId);
            }
        }
        // 오프라인 유저의 데이터 삭제
        redisTemplate.delete(offlineMemberIds.stream().map(s -> HEARTBEAT_HASH + s).toList());
        return new AccessMemberStatus(onlineMemberIds, offlineMemberIds);
    }
}
