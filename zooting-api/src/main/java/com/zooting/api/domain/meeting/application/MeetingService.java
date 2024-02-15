package com.zooting.api.domain.meeting.application;

import com.google.gson.Gson;
import com.zooting.api.domain.BaseEntity;
import com.zooting.api.domain.meeting.dao.MeetingLogRepository;
import com.zooting.api.domain.meeting.dao.WaitingRoomRedisRepository;
import com.zooting.api.domain.meeting.dto.FriendMeetingDto;
import com.zooting.api.domain.meeting.dto.MeetingMemberDto;
import com.zooting.api.domain.meeting.dto.MeetingPickDto;
import com.zooting.api.domain.meeting.dto.response.MeetingMemberRes;
import com.zooting.api.domain.meeting.entity.MeetingLog;
import com.zooting.api.domain.meeting.dto.OppositeGenderParticipantsDto;
import com.zooting.api.domain.meeting.dto.response.OpenviduTokenRes;
import com.zooting.api.domain.meeting.pubsub.MessageType;
import com.zooting.api.domain.meeting.pubsub.RedisPublisher;
import com.zooting.api.domain.meeting.pubsub.WaitingRoomSubscriber;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.SocketBaseDtoRes;
import com.zooting.api.global.common.SocketType;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MemberRepository memberRepository;
    private final MeetingLogRepository meetingLogRepository;
    private final MatchingAlgorithm matchingAlgorithm;
    private final WaitingRoomRedisRepository waitingRoomRedisRepository;
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisPublisher redisPublisher;
    private final WaitingRoomSubscriber waitingRoomSubscriber;
    private final OpenVidu openVidu;
    private final SimpMessageSendingOperations webSocketTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson;

    /**
     * @param userDetails 대기열에 등록하려는 유저의 정보
     * @return 유저가 등록한 대기실의 ID
     */
    public synchronized String registerToWaitingRoom(UserDetails userDetails) {
        Member member = loadMemberFromDatabase(userDetails);
        MeetingMemberDto meetingMemberDto = member.toMeetingMemberDto();

        log.info("미팅: 대기실 등록 요청: 등록 요청한 유저 이메일: {}", userDetails.getUsername());

        Iterable<WaitingRoom> waitingRooms = waitingRoomRedisRepository.findAll();
        WaitingRoom idealWaitingRoom = findIdealWaitingRoom(waitingRooms, meetingMemberDto);
        return registerMemberToWaitingRoom(idealWaitingRoom, meetingMemberDto);
    }

    public synchronized void exitFromWaitingRoom(UserDetails userDetails, String waitingRoomId) {
        Member member = loadMemberFromDatabase(userDetails);
        MeetingMemberDto meetingMemberDto = member.toMeetingMemberDto();

        log.info("미팅: 대기실 퇴장 요청: 퇴장 요청한 유저 이메일{}", userDetails.getUsername());

        WaitingRoom waitingRoom = loadWaitingRoomFromRedis(waitingRoomId);
        Set<MeetingMemberDto> waitingRoomMembers = waitingRoom.getMeetingMembers();

        waitingRoomMembers.remove(meetingMemberDto);

        if (waitingRoomMembers.isEmpty()) {
            waitingRoomRedisRepository.deleteById(waitingRoomId);
        } else { // 아니면 갱신함
            waitingRoomRedisRepository.save(waitingRoom);
        }
    }

    public synchronized void acceptMatching(String waitingRoomId) {
        WaitingRoom waitingRoom = loadWaitingRoomFromRedis(waitingRoomId);
        log.info("미팅: 매칭 대기실 수락: 대기실 아이디: {}", waitingRoomId);

        waitingRoom.setAcceptCount(waitingRoom.getAcceptCount() + 1);
        log.info("미팅: 매칭 대기실 수락 총 개수: {}, 대기실 아이디: {}", waitingRoom.getAcceptCount(), waitingRoomId);
        waitingRoomRedisRepository.save(waitingRoom);

        acceptMatchingMessagePublisher(waitingRoom);
    }

    /**
     * @param meetingMemberDto 대기열에 등록하려는 유저의 정보
     * @return 현재 유저가 들어갈수 있는 가장 이상적인 방
     */
    private WaitingRoom findIdealWaitingRoom(Iterable<WaitingRoom> waitingRooms, MeetingMemberDto meetingMemberDto) {
        // 알고리즘 로직 구현
        if (waitingRooms.iterator().hasNext()) {
            Optional<WaitingRoom> idealWaitingRoom = StreamSupport.stream(waitingRooms.spliterator(), false)
                    .filter(matchingAlgorithm::isUnderMeetingCapacity)
                    .filter(waitingRoom -> matchingAlgorithm.catPassGenderLimit(waitingRoom, meetingMemberDto))
                    .findFirst();

            return idealWaitingRoom.orElseGet(this::createWaitingRoom);
        }
        log.info("미팅: 요청 유저가 입장 가능한 대기실 없음: 유저 아이디: {}", meetingMemberDto.getEmail());
        return createWaitingRoom();
    }

    private WaitingRoom createWaitingRoom() {
        String randomUUID = UUID.randomUUID().toString();
        WaitingRoom waitingRoom = WaitingRoom.builder()
                .waitingRoomId(randomUUID)
                .meetingMembers(new HashSet<>())
                .createdTime(LocalDateTime.now())
                .acceptCount(0)
                .expirationSeconds(-1L)
                .build();

        ChannelTopic channel = new ChannelTopic(MessageType.REDIS_HASH.getPrefix() + randomUUID);
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
        Set<MeetingMemberDto> waitingRoomMembers = Optional.ofNullable(waitingRoom.getMeetingMembers()).orElseThrow(
                () -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_WAITING_ROOM)
        );

        log.info("미팅: 유저가 입장할 대기방의 아이디: {}, 참여자 목록: {}",
                waitingRoom.getWaitingRoomId(), waitingRoomMembers.stream().map(MeetingMemberDto::getEmail).toList().toString());

        waitingRoomMembers.add(meetingMemberDto);
        waitingRoomRedisRepository.save(waitingRoom);

        waitingRoomRegistryMessagePublisher(waitingRoom);

        return waitingRoom.getWaitingRoomId();
    }

    /**
     * 유저의 대기실 입장 정보를 Publish Subscriber에 현재 대기실 인원 수를 전달
     *
     * @param waitingRoom 현재 대기실
     */
    private void waitingRoomRegistryMessagePublisher(WaitingRoom waitingRoom) {
        redisPublisher.publish(MessageType.REDIS_HASH.getPrefix() + waitingRoom.getWaitingRoomId(),
                MessageType.REGISTER.getPrefix() + waitingRoom.getMeetingMembers().size());
    }

    /**
     * 대기방에 있는 유저들의 매칭 수락 정보를 Publish Subscriber에 현재 매칭을 수락한 멤버들의 정보를 전달
     *
     * @param waitingRoom 현재 대기실
     */
    private void acceptMatchingMessagePublisher(WaitingRoom waitingRoom) {
        redisPublisher.publish(MessageType.REDIS_HASH.getPrefix() + waitingRoom.getWaitingRoomId(),
                MessageType.ACCEPTANCE.getPrefix() + waitingRoom.getAcceptCount());
    }

    private Member loadMemberFromDatabase(UserDetails userDetails) {
        Optional<Member> member = memberRepository.findMemberByEmail(userDetails.getUsername());
        return member.orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
    }

    private WaitingRoom loadWaitingRoomFromRedis(String waitingRoomId) {
        Optional<WaitingRoom> waitingRoom = waitingRoomRedisRepository.findById(waitingRoomId);
        return waitingRoom.orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_WAITING_ROOM));
    }

    public OpenviduTokenRes refreshOpenviduToken(String sessionId, String loginEmail) {
        Session session = Optional.ofNullable(openVidu.getActiveSession(sessionId)).orElseThrow(
                () -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        try {
            Connection connection = session.createConnection();
            List<Member> meetingMembers = meetingLogRepository.findMembersByUuidAssociatedWithEmail(loginEmail);
            List<OppositeGenderParticipantsDto> oppositeGenderParticipantsDtoList = meetingMembers.stream()
                    .map(member -> new OppositeGenderParticipantsDto(member.getNickname(), member.getAdditionalInfo().getAnimal()))
                    .collect(Collectors.toList());
            return new OpenviduTokenRes(connection.getToken(), oppositeGenderParticipantsDtoList);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            throw new RuntimeException(e);
        }
    }

    /* 1대1 미팅 신청 */
    public void requestMeeting(String nickname, String loginEmail) {
        Member loginMember = memberRepository.findMemberByEmail(loginEmail).orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Member friend = memberRepository.findMemberByNickname(nickname).orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        sendAcceptMessageToClient(friend, loginMember);
    }

    private void sendAcceptMessageToClient(Member friend, Member loginMember) {
        FriendMeetingDto friendMeetingDto = new FriendMeetingDto(loginMember.getEmail(), loginMember.getNickname());
        log.info("[sendAcceptMessageToClient] email: {} {} {}", friend.getEmail(), loginMember.getEmail(), loginMember.getNickname());
        webSocketTemplate.convertAndSend("/api/sub/" + friend.getEmail(), new SocketBaseDtoRes<>(SocketType.MEETING, friendMeetingDto));
    }

    /* 1대1 미팅 수락 */
    public Map<String, OpenviduTokenRes> sendOpenViduTokenToClient(String nickname, String loginEmail) {
        Member friend = memberRepository.findMemberByNickname(nickname).orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        try {
            Map<String, OpenviduTokenRes> openviduTokenResMap = new HashMap<>();
            List<OppositeGenderParticipantsDto> oppositeGenderParticipantsDtoList = new ArrayList<>();
            oppositeGenderParticipantsDtoList.add(new OppositeGenderParticipantsDto(friend.getNickname(), Objects.nonNull(friend.getAdditionalInfo()) ? friend.getAdditionalInfo().getAnimal() : ""));
            Session session = openVidu.createSession();
            Connection connection = session.createConnection();
            openviduTokenResMap.put(friend.getEmail(), new OpenviduTokenRes(connection.getToken(), oppositeGenderParticipantsDtoList));
            connection = session.createConnection();
            openviduTokenResMap.put(loginEmail, new OpenviduTokenRes(connection.getToken(), oppositeGenderParticipantsDtoList));
            return openviduTokenResMap;
        } catch (OpenViduJavaClientException | OpenViduHttpException ex) {
            throw new RuntimeException(ex);
        }
    }

    /* 화상채팅 종료 시 사람 선택 */
    public Map<String, MeetingPickDto> pickPerson(String nickname, String loginEmail) {
        Member loginMember = memberRepository.findMemberByEmail(loginEmail).orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Member friend = memberRepository.findMemberByNickname(nickname).orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        MeetingPickDto meetingPickDto = new MeetingPickDto(
                loginMember.getNickname(),
                friend.getNickname(),
                Objects.nonNull(loginMember.getAdditionalInfo())? loginMember.getAdditionalInfo().getAnimal() : ""
        );
        return Map.of(friend.getEmail(), meetingPickDto);
    }

    public void picksPerson(String sessionId, String nickname, String loginEmail) {
        Member loginMember = memberRepository.findMemberByEmail(loginEmail).orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Member friend = memberRepository.findMemberByNickname(nickname).orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        MeetingPickDto meetingPickDto = new MeetingPickDto(
                loginMember.getNickname(),
                friend.getNickname(),
                Objects.nonNull(loginMember.getAdditionalInfo())? loginMember.getAdditionalInfo().getAnimal() : ""
        );
        redisTemplate.opsForList().rightPush(sessionId, gson.toJson(meetingPickDto));
        redisTemplate.expire(sessionId, 180L, java.util.concurrent.TimeUnit.SECONDS);
    }

    public List<MeetingPickDto> showResult(String sessionId, String loginEmail) {
        Member loginMember = memberRepository.findMemberByEmail(loginEmail).orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        List<Object> objectList = redisTemplate.opsForList().range(sessionId, 0, -1);
        if (objectList != null && !objectList.isEmpty()) {
            List<MeetingPickDto> meetingPickDtoList = objectList.stream()
                    .map(obj -> gson.fromJson((String) obj, MeetingPickDto.class))
                    .filter(meetingPickDto -> meetingPickDto.pickedNickname().equals(loginMember.getNickname()))
                    .collect(Collectors.toList());
            redisTemplate.expire(sessionId, 180L, java.util.concurrent.TimeUnit.SECONDS);
            return meetingPickDtoList;
        }
        return Collections.emptyList();
    }

    public Map<String, FriendMeetingDto> rejectMeeting(String nickname, String loginEmail) {
        Member loginMember = memberRepository.findMemberByEmail(loginEmail).orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Member friend = memberRepository.findMemberByNickname(nickname).orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        FriendMeetingDto friendMeetingDto = new FriendMeetingDto(loginMember.getEmail(), loginMember.getNickname());
        return Map.of(friend.getEmail(), friendMeetingDto);
    }

    public List<MeetingMemberRes> findRecentMeetingMembers(UserDetails userDetails) {
        String email = userDetails.getUsername();
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(
                () -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));

        List<MeetingLog> meetingLogList = member.getMeetingLogList();

        //가장 최근 로그만 가져옴
        MeetingLog recentMeeting = meetingLogList.stream().max(Comparator.comparing(BaseEntity::getUpdatedAt)).orElseThrow(
                () -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));

        List<MeetingLog> meetingLogListOfMembers = meetingLogRepository.findAllByUuid(recentMeeting.getUuid());

        return meetingLogListOfMembers
                .stream()
                .map(MeetingLog::getMember)
                .filter((meetingMember) -> !meetingMember.getEmail().equals(userDetails.getUsername()))
                .map((meetingMember) -> new MeetingMemberRes(
                        meetingMember.getEmail(),
                        meetingMember.getNickname(),
                        meetingMember.getAdditionalInfo().getAnimal()
                ))
                .toList();
    }
}