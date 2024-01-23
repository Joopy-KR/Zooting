package com.zooting.api.domain.dm.dao;

import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@SpringBootTest
class DMRoomRepositoryTest {

    @Autowired
    DMRoomRepository dmRoomRepository;
    @Autowired
    DMRepository dmRepository;
    @BeforeEach
    void setUp() {
        Member sender = Member.builder().email("a").build();
        Member receiver = Member.builder().email("b").build();
        dmRoomRepository.save(DMRoom.builder().sender(sender).receiver(receiver).build());
        Member sender2 = Member.builder().email("c").build();
        Member receiver2 = Member.builder().email("d").build();
        dmRoomRepository.save(DMRoom.builder().sender(sender2).receiver(receiver2).build());
        dmRoomRepository.save(DMRoom.builder().sender(sender).receiver(receiver2).build());
        dmRoomRepository.save(DMRoom.builder().sender(receiver2).receiver(receiver).build());
    }
    @Test
    @Transactional
    void findBySenderAndReceiver() {
        dmRoomRepository.findAll().forEach(dmRoom -> {
            log.info("{} - {} - {}", dmRoom.getId(), dmRoom.getSender().getEmail(), dmRoom.getReceiver().getEmail());
        });
        DMRoom dmRoom = dmRoomRepository.findBySenderAndReceiver(Member.builder().email("a").build(), Member.builder().email("b").build());
        log.info("{}", dmRoom.getId());
        DMRoom dmRoom2 = dmRoomRepository.findBySenderAndReceiver(Member.builder().email("b").build(), Member.builder().email("a").build());
        log.info("{}", dmRoom2.getId());

    }

    @Test
    @Transactional
    void findDmsByIdTest() {
        // Given
        DMRoom dmRoom = dmRoomRepository.findBySenderAndReceiver(Member.builder().email("a").build(), Member.builder().email("b").build());
        DMRoom dmRoom2 = dmRoomRepository.findBySenderAndReceiver(Member.builder().email("c").build(), Member.builder().email("d").build());

        // Save DMRoom to the database
//        dmRoomRepository.save(dmRoom);
//        dmRoomRepository.save(dmRoom2);
        // Add some DMs to the DMRoom
        for (int i = 0; i < 10; i++) {
            DM dm = DM.builder().dmRoom(dmRoom).message("a send" + i).sender("a").build();
            DM dm2 = DM.builder().dmRoom(dmRoom2).message("c send" + i).sender("c").build();
            dmRepository.save(dm);
            dmRepository.save(dm2);
        }

        // Save DMs to the database
        dmRoomRepository.save(dmRoom);

        // When
        List<DM> dms = dmRoomRepository.findDmsById(dmRoom.getId());

        // Then
        assertNotNull(dms);
        assertEquals(10, dms.size());

        // Log the messages for verification
        dms.forEach(dm -> log.info("Message: {}", dm.getMessage()));
    }

}