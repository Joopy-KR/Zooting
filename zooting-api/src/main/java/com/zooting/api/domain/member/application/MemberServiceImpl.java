package com.zooting.api.domain.member.application;

import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.MembeSearchrRes;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.dto.response.PointRes;
import com.zooting.api.domain.member.entity.AdditionalInfo;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public boolean existNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public boolean checkMemberPrivilege(String userId) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        for (var role : member.getRole()) {
            if (role.equals(Privilege.USER)) {
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public MemberRes findMemberInfo(String memberId) {
        Member member = memberRepository.findMemberByEmail(memberId).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));

        return new MemberRes(
                member.getEmail(),
                member.getGender(),
                member.getNickname(),
                member.getBirth(),
                member.getAddress(),
                member.getPoint(),
                member.getAdditionalInfo().getPersonality(),
                member.getAdditionalInfo().getAnimal(),
                member.getAdditionalInfo().getInterest(),
                member.getAdditionalInfo().getIdealAnimal()
        );
    }

    @Transactional
    @Override
    public void updateMemberInfo(String memberId, MemberReq memberReq) throws ParseException {
        Member member = memberRepository.findMemberByEmail(memberId).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        if (existNickname(memberReq.nickname())) {
            throw new BaseExceptionHandler(ErrorCode.NOT_VALID_ERROR);
        }
        member.setNickname(memberReq.nickname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        member.setBirth(sdf.parse(memberReq.birth()));
        member.setAddress(memberReq.address());
        member.setGender(memberReq.gender());
        member.setPoint(0L); // 추가 정보 저장 시 포인트 0으로 저장

        AdditionalInfo additionalInfo = member.getAdditionalInfo();
        if (Objects.isNull(additionalInfo)) {
            additionalInfo = new AdditionalInfo();
        }
        additionalInfo.setInterest(memberReq.interest().toString());
        additionalInfo.setIdealAnimal(memberReq.idealAnimal().toString());
        additionalInfo.setMember(member);

        // 멤버의 권한 수정 Anonymouse 삭제하고 User 권한 부여
        member.getRole().remove(Privilege.ANONYMOUS);
        member.getRole().add(Privilege.USER);

        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void updateMemberInfo(String memberId, MemberModifyReq memberModifyReq) {
        Member member = memberRepository.findMemberByEmail(memberId).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));

        member.setAddress(memberModifyReq.address());
        member.getAdditionalInfo().setIdealAnimal(memberModifyReq.idealAnimal().toString());

        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void updateInterests(String memberId, InterestsReq additionalReq) {
        Member member = memberRepository.findMemberByEmail(memberId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        AdditionalInfo additionalInfo = member.getAdditionalInfo();
        if (Objects.isNull(additionalInfo)) {
            additionalInfo = new AdditionalInfo();
        }
        additionalInfo.setInterest(additionalReq.interest().toString());
        additionalInfo.setMember(member);
        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void updateIntroduce(String memberId, IntroduceReq introduceReq) {
        Member member = memberRepository.findMemberByEmail(memberId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        AdditionalInfo additionalInfo = member.getAdditionalInfo();
        if (Objects.isNull(additionalInfo)) {
            additionalInfo = new AdditionalInfo();
        }
        additionalInfo.setIntroduce(introduceReq.introduce());
        additionalInfo.setMember(member);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MembeSearchrRes> findMemberList(String userId, String nickname) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));

        // 나를 차단한 유저 리스트 추출
        List<Block> blockList = member.getBlockToList();
        List<Member> findMembers;

        if (!blockList.isEmpty()) {
            List<String> blockMemberNicknames = blockList.stream().map(block -> block.getFrom().getNickname()).toList();
            findMembers = memberRepository.findByNicknameContainingAndNicknameNotIn(nickname, blockMemberNicknames);
        } else {
            findMembers = memberRepository.findMemberByNicknameContaining(nickname);
        }
        return findMembers.stream().map(mem -> new MembeSearchrRes(mem.getNickname(), mem.getEmail())).toList();
    }

    @Transactional
    @Override
    public void updatePersonality(String userId, PersonalityReq personalityReq) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        AdditionalInfo additionalInfo = member.getAdditionalInfo();
        if (Objects.isNull(additionalInfo)) {
            additionalInfo = new AdditionalInfo();
        }
        additionalInfo.setPersonality(personalityReq.personality());
        additionalInfo.setMember(member);
        memberRepository.save(member);
    }


    @Override
    public PointRes findPoints(String userId) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_USER)));
        return new PointRes(member.getPoint());
    }

    @Transactional
    @Override
    public Boolean deductPoints(String userId, Long price) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Long memberPoints = member.getPoint();
        if (memberPoints < price) {
            return false;
        }
        member.setPoint(memberPoints - price);
        memberRepository.save(member);
        return true;
    }
    @Override
    public Member initialMemberRegister(String email) {
        return memberRepository.save(Member
                .builder()
                .role(List.of(Privilege.ANONYMOUS))
                .email(email)
                .build());
    }
}
