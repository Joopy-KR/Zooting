package com.zooting.api.domain.meeting.application;

import com.zooting.api.domain.meeting.dao.WaitingRoomRedisRepository;
import com.zooting.api.domain.meeting.dto.MeetingMemberDto;
import com.zooting.api.domain.meeting.pubsub.MessageType;
import com.zooting.api.domain.meeting.pubsub.RedisPublisher;
import com.zooting.api.domain.meeting.pubsub.WaitingRoomSubscriber;
import com.zooting.api.domain.meeting.redisdto.WaitingRoom;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MemberRepository memberRepository;
    private final WaitingRoomRedisRepository waitingRoomRedisRepository;
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisPublisher redisPublisher;
    private final WaitingRoomSubscriber waitingRoomSubscriber;


    /**
     * TODO: synchronized VS Lettuce의 Spin Lock VS Redisson의 분산락
     *   Mutex / Semaphore / Monitoring (Java의 synchronized 구현 방식)
     *
     * @param userDetails 대기열에 등록하려는 유저의 정보
     * @return 유저가 등록한 대기실의 ID
     */
    public synchronized String registerToWaitingRoom(UserDetails userDetails) {
        log.info("유저를 대기열에 등록합니다. : {}", userDetails.getUsername());

        Member member = loadMemberFromDatabase(userDetails);
        MeetingMemberDto meetingMemberDto = member.toMeetingMemberDto();
        log.info("유저 정보를 가져옵니다. : {}", member.getEmail());

        Iterable<WaitingRoom> waitingRooms = waitingRoomRedisRepository.findAll();
        WaitingRoom idealWaitingRoom = findIdealWaitingRoom(waitingRooms, meetingMemberDto);

        return registerMemberToWaitingRoom(idealWaitingRoom, meetingMemberDto);
    }

    public synchronized void exitFromWaitingRoom(UserDetails userDetails, String waitingRoomId) {
        Member member = loadMemberFromDatabase(userDetails);
        MeetingMemberDto meetingMemberDto = member.toMeetingMemberDto();
        log.info("유저를 대기방에서 퇴장시킵니다 : {}", meetingMemberDto.getEmail());
        log.info("대기방 아이디 : {}", waitingRoomId);

        WaitingRoom waitingRoom = loadWaitingRoomFromRedis(waitingRoomId);
        Set<MeetingMemberDto> waitingRoomMembers = waitingRoom.getMeetingMembers();
        log.info("대기방 정보를 불러옵니다 : {}", waitingRoomMembers.toString());

        waitingRoomMembers.remove(meetingMemberDto);

        if (waitingRoomMembers.isEmpty()) {
            log.info("퇴장을 완료했습니다. 대기방에 유저가 없어 방을 삭제합니다.");
            waitingRoomRedisRepository.deleteById(waitingRoomId);
        } else { // 아니면 갱신함
            log.info("퇴장을 완료했습니다.");
            waitingRoomRedisRepository.save(waitingRoom);
            log.info("퇴장 후 멤버 목록: {}", waitingRoomRedisRepository.findById(waitingRoomId).get().getMeetingMembers().toString());
        }
    }

    /**
     * TODO: meetingMemberDto를 파라미터로 받아 최적의 대기실을 찾는 알고리즘 작성할 것
     *  들어가려는 유저와 동일한 성별의 유저가 2명 이상일 경우 들어가지 못하게 할 것
     *  현재는 대기실에 4명 이하면 무조건 들어가는 상태
     *
     * @param waitingRooms     유저가 들어갈수 있는 방의 목록
     * @param meetingMemberDto 대기열에 등록하려는 유저의 정보
     * @return 현재 유저가 들어갈수 있는 가장 이상적인 방
     */
    private WaitingRoom findIdealWaitingRoom(Iterable<WaitingRoom> waitingRooms, MeetingMemberDto meetingMemberDto) {
        log.info("유저가 입장할 수 있는 방의 정보를 검색합니다: {}", waitingRooms.iterator().hasNext());

        if (waitingRooms.iterator().hasNext()) {
            for (WaitingRoom waitingRoom : waitingRooms) {
                Set<MeetingMemberDto> meetingMembers = waitingRoom.getMeetingMembers();
                if (meetingMembers.size() < 4) {
                    return waitingRoom;
                }
            }
        }
        // 들어갈수 있는 방이 없는 경우 즉, waitingRooms가 비어있거나 상기 if문에서 return되지 않은 경우
        log.info("유저가 입장할수 있는 대기방이 없습니다: {}", waitingRooms);
        return createWaitingRoom(meetingMemberDto);
    }

    private WaitingRoom createWaitingRoom(MeetingMemberDto meetingMemberDto) {
        log.info("새로운 대기방을 생성합니다: {}", meetingMemberDto.getEmail());
        String randomUUID = UUID.randomUUID().toString();

        ChannelTopic channel = new ChannelTopic(MessageType.REDIS_HASH.getPrefix() + randomUUID);

        WaitingRoom waitingRoom = WaitingRoom.builder().waitingRoomId(randomUUID)
                .meetingMembers(new HashSet<>()).acceptCount(0).build();

        redisMessageListener.addMessageListener(waitingRoomSubscriber, channel);
        return waitingRoomRedisRepository.save(waitingRoom);
    }

    /**
     * 유저를 대기실에 등록하고 등록 message publish
     *
     * @param waitingRoom      유저를 등록할 대기실
     * @param meetingMemberDto 대기실에 등록하려는 유저의 정보
     * @return 유저가 등록한 대기실의 ID
     */
    private String registerMemberToWaitingRoom(WaitingRoom waitingRoom, MeetingMemberDto meetingMemberDto) {
        Set<MeetingMemberDto> waitingRoomMembers = waitingRoom.getMeetingMembers();
        log.info("유저가 입장할 대기방의 정보를 가져옵니다: {}", waitingRoomMembers.toString());

        waitingRoomMembers.add(meetingMemberDto);
        waitingRoomRedisRepository.save(waitingRoom);

        waitingRoomRegistryMessagePublisher(waitingRoom);

        return waitingRoom.getWaitingRoomId();
    }

    /**
     * Subscriber에 현재 대기실 인원 수를 전달
     *
     * @param waitingRoom 현재 대기실
     */
    private void waitingRoomRegistryMessagePublisher(WaitingRoom waitingRoom) {
        redisPublisher.publish(MessageType.REDIS_HASH.getPrefix() + waitingRoom.getWaitingRoomId(),
                MessageType.REGISTER.getPrefix() + waitingRoom.getMeetingMembers().size());
    }

    private Member loadMemberFromDatabase(UserDetails userDetails) {
        Optional<Member> member = memberRepository.findMemberByEmail(userDetails.getUsername());
        return member.orElse(null);
    }

    private WaitingRoom loadWaitingRoomFromRedis(String waitingRoomId) {
        log.info("waitingRoomId를 기반으로 대기방 정보를 조회합니다: {} ", waitingRoomId);
        Optional<WaitingRoom> waitingRoom = waitingRoomRedisRepository.findById(waitingRoomId);
        log.info("대기방 정보를 가져왔습니다. false일 경우 대기방 없음: {} ", waitingRoom.isPresent());
        return waitingRoom.orElse(null);
    }
}