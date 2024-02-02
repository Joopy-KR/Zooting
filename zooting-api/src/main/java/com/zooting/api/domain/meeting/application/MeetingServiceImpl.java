package com.zooting.api.domain.meeting.application;

import com.zooting.api.domain.meeting.dto.MemberInfoDTO;
import lombok.Getter;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
public class MeetingServiceImpl implements MeetingService{
    private final StringRedisTemplate redisTemplate;


    public MeetingServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public List<MemberInfoDTO> scanRoomInfo() throws ParseException {
        Set<String> keys = redisTemplate.keys("room:*");
        // oom을 순회하면서 방 정보 조회
        for(String key : keys) {
//            RedisOperations<String, Objects> roomMembers = redisTemplate.opsForList().getOperations();   // redis에서 room/???? : [wjstp14@gmail.com, psj@gmail.com] 이런식으로 옴
            // roomMember를 이메일 리스트로

            // 이메일 리스트로 정보조회



        }
        return null;
    }




}
