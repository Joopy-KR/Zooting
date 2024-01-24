package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.util.MemberFixtureFactory;
import lombok.extern.log4j.Log4j2;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@Log4j2
@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    void extractMembers() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birth = sdf.parse("2000-01-01");
//
//        Member member = Member.builder()
//                .email("test@gmail.com")
//                .birth(birth)
//                .nickname("테스트용")
//                .build();

        var parameters = MemberFixtureFactory.getMemberParams();
        var easyRandom = new EasyRandom(parameters);
        // save members
        List<Member> members = IntStream.range(0, 5)
                .parallel().mapToObj(value ->easyRandom.nextObject(Member.class))
                .toList();
        for (var rs : members) {
            rs.getAdditionalInfo().setIdealAnimal("[토끼, 고양이]");
            rs.getAdditionalInfo().setInterest("[관심사1, 관심사2]");
            memberRepository.save(rs);
            log.info(rs.getEmail(), rs.getBirth(), rs.getAdditionalInfo().getInterest(), rs.getAdditionalInfo().getInterest());
        }
        memberRepository.saveAll(members);




    }
}