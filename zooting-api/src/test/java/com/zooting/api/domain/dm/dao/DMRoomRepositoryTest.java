package com.zooting.api.domain.dm.dao;

import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    @Autowired
    MemberRepository memberRepository;
    @BeforeEach
    void setUp() {
        Member sender = Member.builder().email("a").build();
        Member receiver = Member.builder().email("b").build();
        memberRepository.saveAll(List.of(sender, receiver));

        dmRoomRepository.save(DMRoom.builder().sender(sender).receiver(receiver).build());
        Member sender2 = Member.builder().email("c").build();
        Member receiver2 = Member.builder().email("d").build();
        memberRepository.saveAll(List.of(sender2, receiver2));
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
        Long startCursor = 0L;
        List<DM> dms = dmRoomRepository.findDmsById(dmRoom.getId(), startCursor);
        // Log the messages for verification
        dms.forEach(dm -> log.info("DmId: {} , Message: {}",dm.getId(), dm.getMessage()));
        // Then
        assertNotNull(dms);
        assertEquals(10, dms.size());


    }

    @Test
    @Transactional
    void testFindDmsByIdWithCursor() {
        // Given
        DMRoom dmRoom = dmRoomRepository.findBySenderAndReceiver(Member.builder().email("a").build(), Member.builder().email("b").build());
        DMRoom dmRoom2 = dmRoomRepository.findBySenderAndReceiver(Member.builder().email("c").build(), Member.builder().email("d").build());

        Long roomId = dmRoom.getId();
        Long lastItemId = 105L; // Initial cursor value

        for (int i = 0; i < 10; i++) {
            DM dm = DM.builder().dmRoom(dmRoom).message("a send" + i).sender("a").build();
            DM dm2 = DM.builder().dmRoom(dmRoom2).message("c send" + i).sender("c").build();
            dmRepository.save(dm);
            dmRepository.save(dm2);
        }

        // When
        Pageable pageable = PageRequest.of(0, 10); // Adjust page number and size as needed
        Page<DM> dmPage = dmRoomRepository.findDmsByIdWithCursor(roomId, lastItemId, pageable);

        // Then
        List<DM> dmList = dmPage.getContent();
        dmList.forEach(dm -> {
            // Process each DM item in the current page
            log.info("DM ID: {} , Message: {}", dm.getId(), dm.getMessage());
        });

        // You can assert or perform additional validations as needed
    }

}