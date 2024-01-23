package com.zooting.api.application.usecase;

import com.zooting.api.application.dto.request.MemberAndAnimalfaceReq;
import com.zooting.api.domain.animalface.dao.AnimalFaceRepository;
import com.zooting.api.domain.animalface.entity.AnimalFace;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAndAnimalfaceUsecase {
    final private MemberRepository memberRepository;
    final private AnimalFaceRepository animalFaceRepository;

    @Transactional
    public void saveMemberAndAnimal(String userId, MemberAndAnimalfaceReq animalfaceReq) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        // animalFace 저장 - 동물상 닮은 비율 저장
        AnimalFace animalFace = member.getAnimalFace();
        animalFace.setAnimal1(animalfaceReq.animalfaceList().get(0));
        animalFace.setAnimal2(animalfaceReq.animalfaceList().get(1));
        animalFace.setAnimal3(animalfaceReq.animalfaceList().get(2));
        animalFace.setAnimal4(animalfaceReq.animalfaceList().get(3));
        animalFace.setAnimal5(animalfaceReq.animalfaceList().get(4));

        // 가장 닮은 동물상 저장
        Long maxAnimal = 0L;
        int memberAnimal = 0;
        for(int i = 0; i < 5; i ++ ) {
            if (animalfaceReq.animalfaceList().get(i) > maxAnimal) {
                maxAnimal = animalfaceReq.animalfaceList().get(i);
                memberAnimal = i;
            }
        }
        if (memberAnimal == 0) {
            member.getAdditionalInfo().setAnimal("강아지");
        }else if (memberAnimal == 1) {
            member.getAdditionalInfo().setAnimal("고양이");
        }else if (memberAnimal == 2) {
            member.getAdditionalInfo().setAnimal("토끼");
        }else if (memberAnimal == 3) {
            if (member.getGender().equals("man")) {
                member.getAdditionalInfo().setAnimal("곰");
            } else {
                member.getAdditionalInfo().setAnimal("꼬부기");
            }
        }else {
            if (member.getGender().equals("man")) {
                member.getAdditionalInfo().setAnimal("공룡");
            } else {
                member.getAdditionalInfo().setAnimal("사슴");
            }
        }
        memberRepository.save(member);
        animalFace.setMember(member);
        animalFaceRepository.save(animalFace);

    }



}

