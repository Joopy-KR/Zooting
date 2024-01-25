package com.zooting.api.domain.member.application;

import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.member.dao.ExtractObj;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.*;
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

    @Override
    public MyProfileReq checkMyProfile(String userId, String nickname) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        if (member.getNickname().equals(nickname) ) {
            return new MyProfileReq(true);
        }
        return new MyProfileReq(false);
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
                member.getAdditionalInfo().getIdealAnimal(),
                member.getAdditionalInfo().getBackgroundId(),
                member.getAdditionalInfo().getMaskId()
        );
    }

    @Override
    public MemberRes findMemberInfoByNickname(String nickname) {
        Member member = memberRepository.findMemberByNickname(nickname).orElseThrow(() ->
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
                member.getAdditionalInfo().getIdealAnimal(),
                member.getAdditionalInfo().getBackgroundId(),
                member.getAdditionalInfo().getMaskId()
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
        member.setGender(memberReq.gender().toString());
        member.setPoint(0L); // 추가 정보 저장 시 포인트 0으로 저장

        AdditionalInfo additionalInfo = member.getAdditionalInfo();
        if (Objects.isNull(additionalInfo)) {
            additionalInfo = new AdditionalInfo();
        }
        additionalInfo.setInterest(memberReq.interest().toString());
        additionalInfo.setIdealAnimal(memberReq.idealAnimal().toString());
        additionalInfo.setMember(member);

        // 멤버의 권한 수정 Anonymous 삭제하고 User 권한 부여
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
    public List<MemberSearchRes> findMemberList(String userId, String nickname) {
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
        return findMembers.stream().map(mem -> new MemberSearchRes(mem.getNickname(), mem.getEmail(),
                mem.getGender().toString(), mem.getAdditionalInfo().getAnimal())).toList();
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
    public List<MemberSearchRes> extractMembers(String userId, ExtractingReq extractingReq) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        ExtractObj extractObj = new ExtractObj();
        extractObj.setUserId(userId);
        extractObj.setBlockToList(member.getBlockToList().stream().map(block-> block.getFrom().getEmail()).toList());
        extractObj.setBlockFromList(member.getBlockFromList().stream().map(block-> block.getTo().getEmail()).toList());
        extractObj.setFriendList(member.getFriendList().stream().map(fr-> fr.getFollowing().getEmail()).toList());
        extractObj.setMemberInterests(member.getAdditionalInfo().getInterest().lines().toList());
        extractObj.setMemberIdeals(member.getAdditionalInfo().getIdealAnimal().lines().toList());
        extractObj.setMemberBirth(member.getBirth());
        extractObj.setRangeYear(extractingReq.rangeYear());
        System.out.println(extractObj.getMemberIdeals());
        return memberRepository.extractMatchingMember(extractObj).stream().map(mem -> new MemberSearchRes(mem.getEmail(),mem.getNickname(), mem.getGender().toString(), mem.getAdditionalInfo().getAnimal())).toList();
    }

    @Override
    public List<MemberSearchRes> findMyBlockList(String userId) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        return member.getBlockFromList().stream()
                .map(block -> new MemberSearchRes(member.getEmail(), member.getNickname()
                        , member.getGender().toString(), member.getAdditionalInfo().getAnimal())).toList();


    }
}
