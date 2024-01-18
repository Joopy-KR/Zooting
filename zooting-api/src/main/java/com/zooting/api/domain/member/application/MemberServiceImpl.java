package com.zooting.api.domain.member.application;

import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public List<DMRoom> getDmRooms(String sender) {
        return memberRepository.findDMRoomsByEmail(sender);
    }
    public List<DMRoom> getDmRoomsReverse(String sender) {
        return memberRepository.findDMRoomsReverseByEmail(sender);
    }

}
