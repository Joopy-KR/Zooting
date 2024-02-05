package com.zooting.api.global.jwt.dao;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtRedisDao {
    private final StringRedisTemplate redisTemplate;
    private static final String REFRESH_HASH = "refresh-token/";

    public void save(String email, String refreshToken) {
        redisTemplate.opsForValue().set(REFRESH_HASH + email, refreshToken, 15, TimeUnit.DAYS);
    }
    public String get(String email) {
        return redisTemplate.opsForValue().get(REFRESH_HASH + email);
    }
}