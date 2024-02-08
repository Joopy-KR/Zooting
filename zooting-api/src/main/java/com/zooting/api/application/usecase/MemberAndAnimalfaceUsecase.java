package com.zooting.api.application.usecase;

import com.zooting.api.application.dto.request.MemberAndAnimalfaceReq;
import com.zooting.api.domain.animalface.dao.AnimalFaceRepository;
import com.zooting.api.domain.animalface.entity.AnimalFace;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import com.zooting.api.global.jwt.dto.TokenDto;
import com.zooting.api.global.jwt.service.JwtService;
import com.zooting.api.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberAndAnimalfaceUsecase {
    final private MemberRepository memberRepository;
    final private AnimalFaceRepository animalFaceRepository;
    private final JwtService jwtService;
    public static final Long DEFAULT_ANIMAL_MODIFY_PRICE = 50L;

    @Transactional
    public TokenDto saveMemberAndAnimal(UserDetails userDetails, MemberAndAnimalfaceReq animalfaceReq) {
        Member member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        // 동물상 변경하는 유저 - 잔여 포인트 확인
        if (member.getRole().contains(Privilege.USER)) {
            if (member.getPoint() < DEFAULT_ANIMAL_MODIFY_PRICE) {
                throw new BaseExceptionHandler(ErrorCode.NOT_ENOUGH_POINT); // 포인트 부족 에러
            }
            member.setPoint(member.getPoint() - DEFAULT_ANIMAL_MODIFY_PRICE);
        }
        // animalFace 저장 - 동물상 닮은 비율 저장
        AnimalFace animalFace = member.getAnimalFace();
        if (Objects.isNull(animalFace)) {
            animalFace = new AnimalFace();
        }

        Long maxAnimal = 0L;
        String memberAnimal = "";
        for (var af : animalfaceReq.animalFaceReqList()) {
            switch (af.animal()) {
                case "강아지" -> animalFace.setAnimal1(af.percentage());
                case "고양이" -> animalFace.setAnimal2(af.percentage());
                case "토끼" -> animalFace.setAnimal3(af.percentage());
                case "사슴", "곰" -> animalFace.setAnimal4(af.percentage());
                case "펭귄", "공룡" -> animalFace.setAnimal5(af.percentage());
                default -> throw new BaseExceptionHandler(ErrorCode.INVALID_TYPE_VALUE);
            }
            if (af.percentage() > maxAnimal) {
                maxAnimal = af.percentage();
                memberAnimal = af.animal();
            }
        }

        // 가장 닮은 동물상 저장
        member.getAdditionalInfo().setAnimal(memberAnimal);

        TokenDto tokenDto = createNewToken(userDetails, member);
        memberRepository.save(member);
        animalFace.setMember(member);
        animalFaceRepository.save(animalFace);
        return tokenDto;
    }

    // 새로운 토큰 생성
    private TokenDto createNewToken(UserDetails userDetails, Member member) {
        // 멤버의 권한 수정 Anonymous 삭제하고 User 권한 부여
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_" + Privilege.ANONYMOUS)) {
                member.getRole().remove(Privilege.ANONYMOUS);
                if (!member.getRole().contains(Privilege.USER)) {
                    member.getRole().add(Privilege.USER);
                }

                if (userDetails instanceof CustomUserDetails) {
                    // ANONYMOUS 권한 제거
                    userDetails.getAuthorities().removeIf(auth ->
                            auth.getAuthority().equals("ROLE_" + Privilege.ANONYMOUS));

                    Collection<GrantedAuthority> newAuth = new ArrayList<>();
                    newAuth.add(new SimpleGrantedAuthority(Privilege.USER.name()));
                    // USER 권한 추가
                    CustomUserDetails user = CustomUserDetails.builder()
                            .email(userDetails.getUsername())
                            .nickname(((CustomUserDetails) userDetails).getNickname())
                            .authorities(newAuth)
                            .build();

                    return new TokenDto(
                            jwtService.createAccessToken(user),
                            jwtService.createRefreshToken(user)
                    );
                }
            }
        }
        return null;
    }
}

