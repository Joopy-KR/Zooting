package com.zooting.api.domain.dm.application;

import com.zooting.api.domain.dm.dao.DMRepository;
import com.zooting.api.domain.dm.dao.DMRoomRepository;
import com.zooting.api.domain.dm.dto.DMDto;
import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.file.entity.File;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DMServiceImpl implements DMService{
    private final DMRepository dmRepository;
    private final DMRoomRepository dmRoomRepository;
    private final MemberRepository memberRepository;
    @Override
    public DMRoom getDMRoom(String sender, String receiver) {
        //ToDO : 입력받은 값에 대해 검증 -> sender 는 로그인한 사람 receiver만 검증
        Member newSender = Member.builder().email(sender).build();
        Member newReceiver = memberRepository.findByEmail(receiver).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));;
        DMRoom dmRoom = dmRoomRepository.findBySenderAndReceiver(newSender, newReceiver);
        return Objects.nonNull(dmRoom) ? dmRoom : createDMRoom(sender, receiver); // DM방이 없을 경우 생성
    }

    @Transactional
    public DMRoom createDMRoom(String sender, String receiver){
        Member newSender = Member.builder().email(sender).build();
        Member newReceiver = Member.builder().email(receiver).build();
        DMRoom newDMRoom = DMRoom.builder().sender(newSender).receiver(newReceiver).build();
        dmRoomRepository.save(newDMRoom); // 만약 save 가 실패하면?
        return newDMRoom;
    }
    @Override
    public List<DM> getDMList(Long dmRoomId) {
        return dmRoomRepository.findDmsById(dmRoomId);
    }

    @Override
    public List<File> getDmFiles(Long id) {
        return dmRepository.getFilesById(id);
    }

    @Override
    @Async
    @Transactional
    public void saveDM(DMDto dmDto) {
        DM dm = new DM();
        DMRoom dmRoom = new DMRoom();
        dmRoom.setId(dmDto.dmRoomId());
        dm.setDmRoom(dmRoom);
        dm.setMessage(dmDto.message());
        dm.setSender(dmDto.sender());
        dmRepository.save(dm);
    }
}
