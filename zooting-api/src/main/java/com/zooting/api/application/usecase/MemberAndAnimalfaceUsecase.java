package com.zooting.api.application.usecase;

import com.zooting.api.application.dto.request.MemberAndAnimalfaceReq;
import com.zooting.api.domain.animalface.dao.AnimalFaceRepository;
import com.zooting.api.domain.animalface.entity.AnimalFace;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberAndAnimalfaceUsecase {
    final private MemberRepository memberRepository;
    final private AnimalFaceRepository animalFaceRepository;
    public static final Long DEFAULT_ANIMAL_MODIFY_PRICE = 50L;
    @Transactional
    public boolean saveMemberAndAnimal(String userId, MemberAndAnimalfaceReq animalfaceReq) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        // 동물상 변경하는 유저 - 잔여 포인트 확인
        if (member.getRole().contains(Privilege.USER)) {
            if (member.getPoint() < DEFAULT_ANIMAL_MODIFY_PRICE) {
                return false;
            }
            member.setPoint(member.getPoint()-DEFAULT_ANIMAL_MODIFY_PRICE);
        }
        // animalFace 저장 - 동물상 닮은 비율 저장
        AnimalFace animalFace = member.getAnimalFace();
        if (Objects.isNull(animalFace)){
            animalFace = new AnimalFace();
        }

        Long maxAnimal = 0L;
        String memberAnimal = "";
        for (var af : animalfaceReq.animalFaceReqList()) {
            if (af.animal().equals("강아지")) {
                animalFace.setAnimal1(af.percentage());
            }else if (af.animal().equals("고양이")) {
                animalFace.setAnimal2(af.percentage());
            }else if (af.animal().equals("토끼")) {
                animalFace.setAnimal3(af.percentage());
            }else if (af.animal().equals("사슴") || af.animal().equals("곰")) {
                animalFace.setAnimal4(af.percentage());
            }else {
                animalFace.setAnimal5(af.percentage());
            }
            if (af.percentage() > maxAnimal) {
                maxAnimal = af.percentage();
                memberAnimal = af.animal();
            }
        }

        // 가장 닮은 동물상 저장
        member.getAdditionalInfo().setAnimal(memberAnimal);
        // 멤버의 권한 수정 Anonymous 삭제하고 User 권한 부여
        if (member.getRole().contains(Privilege.ANONYMOUS)) {
            member.getRole().remove(Privilege.ANONYMOUS);
            member.getRole().add(Privilege.USER);
        }
        memberRepository.save(member);
        animalFace.setMember(member);
        animalFaceRepository.save(animalFace);
        return true;
    }
}

