package com.zooting.api.domain.dm.application;

import com.zooting.api.domain.dm.dao.DMRepository;
import com.zooting.api.domain.dm.dao.DMRoomRepository;
import com.zooting.api.domain.dm.dto.DMDto;
import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.file.entity.File;
import com.zooting.api.domain.member.entity.Member;
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
    @Override
    public DMRoom getDMRoom(String sender, String receiver) {
        //ToDO : 입력받은 값에 대해 검증
        log.info("sender {} receiver {}", sender, receiver);
        Member newSender = new Member();
        newSender.setEmail(sender);
        log.info("newSender {}", newSender.getEmail());
        Member newReceiver = new Member();
        newReceiver.setEmail(receiver);
        log.info("newReceiver {}", newReceiver.getEmail());
        DMRoom dmRoom = dmRoomRepository.findBySenderAndReceiver(newSender, newReceiver);
//        for(DM dm : dmRoom.getDms()){
//            log.info("{}", dm.getMessage());
//        }
        return Objects.nonNull(dmRoom) ? dmRoom : createDMRoom(sender, receiver); // DM방이 없을 경우 생성
    }

    @Transactional
    public DMRoom createDMRoom(String sender, String receiver){
        //ToDO : 입력받은 값에 대해 검증
        log.info("sender {} receiver {}", sender, receiver);
        Member newSender = new Member();
        newSender.setEmail(sender);
        Member newReceiver = new Member();
        newReceiver.setEmail(receiver);
        DMRoom newDMRoom = DMRoom.builder().sender(newSender).receiver(newReceiver).build();
        DMRoom newDMRoomReverse = DMRoom.builder().sender(newReceiver).receiver(newSender).build();
        dmRoomRepository.save(newDMRoom); // 만약 save 가 실패하면?
        dmRoomRepository.save(newDMRoomReverse); // 만약 save 가 실패하면?
        return newDMRoom;
    }
    @Override
    public List<DM> getDMList(DMRoom dmRoom) {
        return dmRoomRepository.findDmsById(dmRoom.getId());
    }

    @Override
    public List<File> getDmFiles(Long id) {
        return dmRepository.getFilesById(id);
    }

    @Override
    @Async
    @Transactional
    public void saveDM(DMDto dmDto) {
        DMRoom dmRoom = getDMRoom(dmDto.sender(), dmDto.receiver());
        DM dm = new DM();
        dm.setDmRoom(dmRoom);
        dm.setMessage(dmDto.message());
        dm.setSender(dmDto.sender());
        dmRepository.save(dm);
    }
}
