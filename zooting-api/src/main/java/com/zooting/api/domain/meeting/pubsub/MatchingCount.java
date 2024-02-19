package com.zooting.api.domain.meeting.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class MatchingCount implements ApplicationRunner {
    private final RedisTemplate<String, Object> redisTemplate;

    /* 서버 시작 시 매칭인원 0으로 초기화 */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        redisTemplate.opsForValue().set("matchingCount", "0");
        log.info("matchingCount 초기화 완료");
    }
}
