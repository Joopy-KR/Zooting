package com.zooting.api.domain.dm.application;

import com.zooting.api.domain.dm.dao.DMRepository;
import com.zooting.api.domain.dm.dao.DMRoomRepository;
import com.zooting.api.domain.dm.dto.request.DMReq;
import com.zooting.api.domain.dm.dto.response.DMDto;
import com.zooting.api.domain.dm.dto.response.DMRoomRes;
import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.file.dao.FileRepository;
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

import java.util.ArrayList;
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
                .map(file ->{
                        File savedFile = File.builder()
                                .dm(dm)
                                .fileName(file.fileName())
                                .img_url(file.imgUrl())
                                .build();
                        fileRepository.save(savedFile);
                        return savedFile;
                        }).toList();
        dm.setFiles(files);
        dmRepository.save(dm);
    }

    @Override
    @Transactional
    public DMRoomRes enterDMRoom(String sender, String receiver) {
        DMRoom dmRoom = getDMRoom(sender, receiver);
        Long cursor = getStartCursor(dmRoom.getId(), sender);
        List<DM> dmList = getAllDMList(dmRoom.getId(), cursor);
        if (dmList.size() != 0) {
            dmRoom.setSenderLastReadId(dmList.get(dmList.size() - 1).getId());
            cursor = dmList.get(dmList.size() - 1).getId();
        }
        List<DMDto> dmDtoList = dmList
                .stream()
                .map(dm -> new DMDto(dm.getId(), sender, dm.getMessage()))
                .toList();
        return new DMRoomRes(dmRoom.getId(), dmDtoList, cursor);
    }

    @Override
    public DMRoomRes getDMRoomWithCursor(Long dmRoomId, Long cursor) {
        Page<DM> dmList = getDMList(dmRoomId, cursor);
        List<DMDto> dmDtoList = dmList
                .stream()
                .map(dm -> new DMDto(dm.getId(), dm.getSender(), dm.getMessage()))
                .toList();
        return new DMRoomRes(
                dmRoomId,
                dmDtoList,
                dmDtoList.size() > 0 ? dmDtoList.get(dmDtoList.size() - 1).dmRoomId() : 0
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
