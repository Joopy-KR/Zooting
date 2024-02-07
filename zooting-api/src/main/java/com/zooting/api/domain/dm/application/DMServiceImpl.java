package com.zooting.api.domain.dm.application;

import com.zooting.api.domain.dm.dao.DMRepository;
import com.zooting.api.domain.dm.dao.DMRoomRepository;
import com.zooting.api.domain.dm.dto.request.DMReq;
import com.zooting.api.domain.dm.dto.response.DMDto;
import com.zooting.api.domain.dm.dto.response.DMRoomRes;
import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.file.dao.FileRepository;
import com.zooting.api.domain.file.dto.response.DMFileRes;
import com.zooting.api.domain.file.entity.File;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DMServiceImpl implements DMService {
    private final DMRepository dmRepository;
    private final DMRoomRepository dmRoomRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public DMRoom getDMRoom(String sender, String receiver) {
        //ToDO : 입력받은 값에 대해 검증 -> sender 는 로그인한 사람 receiver만 검증
        Member newSender = Member.builder().email(sender).build();
        Member newReceiver = memberRepository.findByEmail(receiver).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        DMRoom dmRoom = dmRoomRepository.findBySenderAndReceiver(newSender, newReceiver);
        return Objects.nonNull(dmRoom) ? dmRoom : createDMRoom(sender, receiver); // DM방이 없을 경우 생성
    }

    @Transactional
    public DMRoom createDMRoom(String sender, String receiver) {
        Member newSender = Member.builder().email(sender).build();
        Member newReceiver = Member.builder().email(receiver).build();
        DMRoom newDMRoom = DMRoom.builder().sender(newSender).receiver(newReceiver).senderLastReadId(0L).receiverLastReadId(0L).build();
        dmRoomRepository.save(newDMRoom); // 만약 save 가 실패하면?
        return newDMRoom;
    }

    @Override
    public List<DM> getAllDMList(Long dmRoomId, Long startCursor) {
        return dmRoomRepository.findDmsById(dmRoomId, startCursor);
    }

    @Override
    public Page<DM> getDMList(Long dmRoomId, Long cursor) {
        Pageable pageable = PageRequest.of(0, 10);
        return dmRepository.findByDmRoomIdAndIdLessThanOrderByIdDesc(dmRoomId, cursor, pageable);
    }

    @Override
    @Async
    @Transactional
    public void saveDM(DMReq dmReq) {
        DM dm = new DM();
        DMRoom dmRoom = new DMRoom();
        dmRoom.setId(dmReq.dmRoomId());
        dm.setDmRoom(dmRoom);
        dm.setMessage(dmReq.message());
        dm.setSender(dmReq.sender());
        List<File> files = dmReq.files()
                .stream()
                .map(file -> {
                    File savedFile = File.builder()
                            .dm(dm)
                            .fileName(file.fileName())
                            .imgUrl(file.imgUrl())
                            .fileDir(file.fileDir())
                            .thumbnailUrl(file.thumbnailUrl())
                            .originFileName(file.originFileName())
                            .build();
                    fileRepository.save(savedFile);
                    return savedFile;
                }).toList();
        dm.setFiles(files);
        dmRepository.save(dm);
        RedisDMReq redisDMReq = new RedisDMReq(dmReq.dmRoomId(), dm.getId(), "MESSAGE", dmReq.message(), dmReq.sender(), dmReq.receiver(),
                dmReq.files().stream().map(file -> new DMFileRes(file.imgUrl(), file.thumbnailUrl())).toList(), dm.getCreatedAt(), dm.getUpdatedAt());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(redisDMReq.getClass()));
        redisTemplate.opsForList().rightPush("dmRoomId:" + dmReq.dmRoomId(), redisDMReq);
        redisTemplate.expire("dmRoomId:" + dmReq.dmRoomId(), 30, java.util.concurrent.TimeUnit.MINUTES);
        //GSON?
    }

    @Override
    @Transactional
    public DMRoomRes enterDMRoom(String sender, String receiver) {
        DMRoom dmRoom = getDMRoom(sender, receiver);
        Long cursor = getStartCursor(dmRoom.getId(), sender);
        List<DM> dmList = getAllDMList(dmRoom.getId(), cursor);
        if (!dmList.isEmpty()) {
            dmRoom.setSenderLastReadId(dmList.get(dmList.size() - 1).getId());
            cursor = dmList.get(dmList.size() - 1).getId();
        }
        List<DMDto> dmDtoList = dmList
                .stream()
                .map(dm -> new DMDto(
                                dmRoom.getId(), dm.getId(), dm.getSender(), dm.getMessage(), dm.getFiles()
                                .stream()
                                .map(file -> new DMFileRes(
                                        file.getImgUrl(),
                                        file.getThumbnailUrl()
                                ))
                                .toList()
                        )
                )
                .toList();
        return new DMRoomRes(dmRoom.getId(), dmDtoList, cursor);
    }

    @Override
    @Transactional
    public RedisDMRoomRes enterDMRoomRedis(String sender, String receiver) {
        DMRoom dmRoom = getDMRoom(sender, receiver);
        Long cursor = getStartCursor(dmRoom.getId(), sender);
        //redis에 데이터가 있다면 불러옴
        List<Object> ObjectList = redisTemplate.opsForList().range("dmRoomId:" + dmRoom.getId(), 0, -1);
        if (ObjectList != null && !ObjectList.isEmpty()) {
            List<RedisDMReq> redisDMReqList = ObjectList.stream()
                    .map(obj -> (RedisDMReq) obj)
                    .collect(Collectors.toList());
            return new RedisDMRoomRes(dmRoom.getId(), redisDMReqList, cursor);
        }
        List<DM> dmList = getAllDMList(dmRoom.getId(), cursor);
        if (!dmList.isEmpty()) {
            dmRoom.setSenderLastReadId(dmList.get(dmList.size() - 1).getId());
            cursor = dmList.get(dmList.size() - 1).getId();
        }
        List<RedisDMReq> redisDMReqList = dmList
                .stream()
                .map(dm -> new RedisDMReq(
                                dm.getDmRoom().getId(), dm.getId(), "MESSAGE", dm.getMessage(), dm.getSender(), receiver, dm.getFiles()
                                .stream()
                                .map(file -> new DMFileRes(
                                        file.getImgUrl(),
                                        file.getThumbnailUrl()
                                ))
                                .toList(),
                                dm.getCreatedAt(), dm.getUpdatedAt()
                        )
                )
                .toList();
        redisTemplate.opsForList().rightPushAll("dmRoomId:" + dmRoom.getId(), redisDMReqList);
        return new RedisDMRoomRes(dmRoom.getId(), redisDMReqList, cursor);
    }

    @Override
    public DMRoomRes getDMRoomWithCursor(Long dmRoomId, Long cursor) {
        Page<DM> dmList = getDMList(dmRoomId, cursor);
        List<DMDto> dmDtoList = dmList
                .stream()
                .map(dm -> new DMDto(
                                dmRoomId, dm.getId(), dm.getSender(), dm.getMessage(), dm.getFiles()
                                .stream()
                                .map(file -> new DMFileRes(
                                        file.getImgUrl(),
                                        file.getThumbnailUrl()
                                ))
                                .toList()
                        )
                )
                .toList();
        return new DMRoomRes(
                dmRoomId,
                dmDtoList,
                !dmDtoList.isEmpty() ? dmDtoList.get(dmDtoList.size() - 1).dmId() : 0
        );
    }

    @Transactional
    @Override
    public void exitDmRoom(Long dmRoomId, String loginEmail) {
        DMRoom dmRoom = dmRoomRepository.findById(dmRoomId).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        List<DM> dmList = dmRoom.getDms();
        log.info("dmListsize : {}", dmList.size());
        int index = dmList.size() - 1;
        log.info("lastdmid : {}", dmList.get(index).getId());
        if (!dmList.isEmpty()) {
            if (dmRoom.getSender().getEmail().equals(loginEmail)) {
                dmRoom.setSenderLastReadId(dmList.get(index).getId());
            } else {
                dmRoom.setReceiverLastReadId(dmList.get(index).getId());
            }
        }
    }

    @Override
    public RedisDMRoomRes getDMRoomWithCursorRedis(Long dmRoomId, Long cursor) {
        Page<DM> dmList = getDMList(dmRoomId, cursor);
        List<RedisDMReq> redisDMReqList = dmList
                .stream()
                .map(dm -> new RedisDMReq(
                                dm.getDmRoom().getId(), dm.getId(), "MESSAGE", dm.getMessage(), dm.getSender(), dm.getDmRoom().getReceiver().getEmail(), dm.getFiles()
                                .stream()
                                .map(file -> new DMFileRes(
                                        file.getImgUrl(),
                                        file.getThumbnailUrl()
                                ))
                                .toList(),k
                                dm.getCreatedAt(), dm.getUpdatedAt()
                        )
                )
                .toList();
        redisTemplate.opsForList().leftPushAll("dmRoomId:" + dmRoomId, redisDMReqList);
        return new RedisDMRoomRes(
                dmRoomId,
                redisDMReqList,
                !redisDMReqList.isEmpty() ? redisDMReqList.get(redisDMReqList.size() - 1).dmId() : 0
        );
    }

    private Long getStartCursor(Long dmRoomId, String sender) {
        DMRoom dmRoom = dmRoomRepository.findById(dmRoomId).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        if (dmRoom.getSender().getEmail().equals(sender)) {
            return dmRoom.getSenderLastReadId();
        }
        return dmRoom.getReceiverLastReadId();
    }
}
