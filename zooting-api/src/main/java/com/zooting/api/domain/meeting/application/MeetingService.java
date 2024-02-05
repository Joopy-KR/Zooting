package com.zooting.api.domain.meeting.application;

import com.zooting.api.domain.meeting.dao.WaitingRoomRedisRepository;
import com.zooting.api.domain.meeting.dto.MeetingMemberDto;
import com.zooting.api.domain.meeting.redisdto.WaitingRoom;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MemberRepository memberRepository;
    private final WaitingRoomRedisRepository waitingRoomRedisRepository;

    //TODO: 동시성 문제가 발생할 수도 있지 않을까?
    // synchronized를 사용했지만, 로드 밸런싱 등으로 서버 인스턴스가 2개 이상이면 똑같이 문제가 발생할수 있다.
    // why? -> 동일한 Redis를 이용하고 있으므로. 일단 현재는 서버 하나만 띄울거니까 synchronized로 대체.
    // Redisson을 이용하면 분산 락을 적용해볼수도 있다.
    // Lettuce에서도 분산락을 지원하지만 스핀락 방식이라 오버헤드가 크다.
    public synchronized String registerToWaitingRoom(UserDetails userDetails) {
        // 대기열에 등록하려는 유저의 정보를 불러옴
        Optional<Member> member = memberRepository.findMemberByEmail(userDetails.getUsername());

        if (member.isPresent()) {
            MeetingMemberDto meetingMemberDto = member.get().toMeetingMemberDto();
            Iterable<WaitingRoom> waitingRooms = waitingRoomRedisRepository.findAll();

            for (WaitingRoom waitingRoom : waitingRooms) {
                Set<MeetingMemberDto> meetingMembers = waitingRoom.getMeetingMembers();

                if(meetingMembers.size() < 4) {
                    //TODO: 매칭 로직 수행. 현재는 인원 4 이하면 무조건 참가
                    meetingMembers.add(meetingMemberDto);
                    waitingRoomRedisRepository.save(waitingRoom);
                    return waitingRoom.getRoomId();
                }
            }
        }
        return null;
    }
}
