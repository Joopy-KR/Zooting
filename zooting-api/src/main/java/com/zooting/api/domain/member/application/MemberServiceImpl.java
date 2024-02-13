package com.zooting.api.domain.member.application;

import com.zooting.api.domain.background.dao.BackgroundInventoryRepository;
import com.zooting.api.domain.background.entity.Background;
import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.mask.dao.MaskInventoryRepository;
import com.zooting.api.domain.mask.entity.Mask;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final MaskInventoryRepository maskInventoryRepository;
    private final BackgroundInventoryRepository backgroundInventoryRepository;
    public static final String DEFAULT_MASK = "https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/animal_group.png";
    public static final String DEFAULT_BACKGROUND = "https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/oilpaintart.jpg";
    public static final Long DEFAULT_MASK_ID = 100L;
    public static final Long DEFAULT_BACKGROUND_ID = 100L;
    public static final Long DEFAULT_POINT = 1000L;
    public static final Long CHANGE_NICKNAME_PRICE = 100L;

    @Override
    public boolean existNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Privilege> checkMemberPrivilege(String userId) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        return member.getRole();
    }

    @Override
    public MyProfileReq checkMyProfile(String userId, String nickname) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        if (member.getNickname().equals(nickname)) {
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
                member.getAdditionalInfo().getIntroduce(),
                member.getAdditionalInfo().getPersonality(),
                member.getAdditionalInfo().getAnimal(),
                member.getAdditionalInfo().getInterest(),
                member.getAdditionalInfo().getIdealAnimal(),
                member.getAdditionalInfo().getBackgroundId(),
                member.getAdditionalInfo().getBackgroundUrl(),
                member.getAdditionalInfo().getMaskId(),
                member.getAdditionalInfo().getMaskUrl(),
                null // 나 자신 조회의 경우 상태 관리 필요 없음
        );
    }

    @Transactional(readOnly = true)
    @Override
    public MemberRes findMemberInfoByNickname(String userId, String nickname) {
        // 상대방 확인
        Member member = memberRepository.findMemberByNickname(nickname).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        // 내 객체 생성
        var me = Member.builder().email(userId).build();

        return new MemberRes(
                member.getEmail(),
                member.getGender(),
                member.getNickname(),
                member.getBirth(),
                member.getAddress(),
                null, // 상대방 조회의 경우 포인트 조회 X
                member.getAdditionalInfo().getIntroduce(),
                member.getAdditionalInfo().getPersonality(),
                member.getAdditionalInfo().getAnimal(),
                member.getAdditionalInfo().getInterest(),
                member.getAdditionalInfo().getIdealAnimal(),
                member.getAdditionalInfo().getBackgroundId(),
                member.getAdditionalInfo().getBackgroundUrl(),
                member.getAdditionalInfo().getMaskId(),
                member.getAdditionalInfo().getMaskUrl(),
                memberRepository.findByNicknameWithStatus(me, member)
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
        member.setPoint(DEFAULT_POINT); // 추가 정보 저장 시 포인트 0으로 저장

        AdditionalInfo additionalInfo = member.getAdditionalInfo();
        if (Objects.isNull(additionalInfo)) {
            additionalInfo = new AdditionalInfo();
        }
        additionalInfo.setInterest(memberReq.interest().toString());
        additionalInfo.setIdealAnimal(memberReq.idealAnimal().toString());

        // 디폴트 마스크, 배경 이미지로 저장
        additionalInfo.setMaskUrl(DEFAULT_MASK);
        additionalInfo.setBackgroundUrl(DEFAULT_BACKGROUND);
        additionalInfo.setMaskId(DEFAULT_MASK_ID);
        additionalInfo.setBackgroundId(DEFAULT_BACKGROUND_ID);
        additionalInfo.setMember(member);

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
        additionalInfo.setIdealAnimal(additionalReq.idealAnimal().toString());
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

    @Transactional
    @Override
    public boolean changeMask(String memberId, MaskReq maskReq) {
        AdditionalInfo memberInfo = memberRepository.findMemberByEmail(memberId).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER)).getAdditionalInfo();
        Mask myMask = maskInventoryRepository.findByMask_Id(maskReq.maskId()).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR)).getMask();
        if (myMask.getAnimal().equals(memberInfo.getAnimal())) {
            memberInfo.setMaskId(myMask.getId());
            memberInfo.setMaskUrl(myMask.getFile().getImgUrl());
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void changeBackground(String memberId, BackgroundReq backgroundReq) {
        AdditionalInfo memberInfo = memberRepository.findMemberByEmail(memberId).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER)).getAdditionalInfo();
        Background myBackground = backgroundInventoryRepository.findBackgroundInventoryByBackground_Id(backgroundReq.backgroundId()).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR)).getBackground();
        memberInfo.setBackgroundId(myBackground.getId());
        memberInfo.setBackgroundUrl(myBackground.getFile().getImgUrl());
    }

    @Transactional
    @Override
    public boolean modifyNickname(String memberId, NicknameReq nicknameReq) {
        Member member = memberRepository.findMemberByEmail(memberId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        // 닉네임 중복 체크 && 잔여 포인트 확인
        if (!memberRepository.existsByNickname(nicknameReq.nickname()) && member.getPoint() >= CHANGE_NICKNAME_PRICE) {
            // 닉네임 변경
            member.setNickname(nicknameReq.nickname());
            // 포인트 차감
            member.setPoint(member.getPoint() - CHANGE_NICKNAME_PRICE);
            memberRepository.save(member);
            return true;
        }
        return false;
    }


    @Transactional(readOnly = true)
    @Override
    public MemberSearchPageRes findMemberList(Pageable pageable, String userId, String nickname) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Page<MemberSearchRes> findMembers;

        // 나를 차단한 유저 리스트 추출
        List<Block> blockList = member.getBlockToList();

        if (!blockList.isEmpty()) {
            List<String> blockMemberNicknames = blockList.stream().map(block -> block.getFrom().getNickname()).toList();
            if (Objects.isNull(nickname)) {
                findMembers = memberRepository.findMembersBy(pageable);
            } else {
                findMembers = memberRepository.findByNicknameContainingAndNicknameNotIn(pageable, nickname, blockMemberNicknames);
            }
        } else {
            if (Objects.isNull(nickname)) {
                findMembers = memberRepository.findMembersBy(pageable);
            } else {
                findMembers = memberRepository.findMemberByNicknameContaining(pageable, nickname);
            }
        }
        return new MemberSearchPageRes(findMembers.getContent(), findMembers.getNumber(), findMembers.getTotalPages());

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
    public PointRes findPoints(String memberId) {
        Member member = memberRepository.findMemberByEmail(memberId)
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_USER)));
        return new PointRes(member.getPoint());
    }

    @Transactional
    @Override
    public void deductPoints(String userId, Long price) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Long memberPoints = member.getPoint();
        if (memberPoints < price) {
            member.setPoint(0L);
        }
        member.setPoint(memberPoints - price);
        memberRepository.save(member);
    }
    @Transactional
    @Override
    public void addPoints(String userId, Long points) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Long memberPoints = member.getPoint();
        member.setPoint(memberPoints + points);
        memberRepository.save(member);
    }

    @Override
    public List<MemberSearchRes> extractMembers(String userId, ExtractingReq extractingReq) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        ExtractObj extractObj = new ExtractObj();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        extractObj.setMemberBirthYear(member.getBirth().getYear() + 1900);
        extractObj.setUserId(userId);
        extractObj.setBlockToList(member.getBlockToList().stream().map(block -> block.getFrom().getEmail()).toList());
        extractObj.setBlockFromList(member.getBlockFromList().stream().map(block -> block.getTo().getEmail()).toList());
        extractObj.setFriendList(member.getFriendList().stream().map(fr -> fr.getFollowing().getEmail()).toList());
        extractObj.setMemberInterests(member.getAdditionalInfo().getInterest().lines().toList());
        extractObj.setMemberIdeals(member.getAdditionalInfo().getIdealAnimal().lines().toList());
        extractObj.setRangeYear(extractingReq.rangeYear());
        System.out.println(extractObj.getMemberIdeals());
        return memberRepository.extractMatchingMember(extractObj).stream().map(mem -> new MemberSearchRes(mem.getNickname(), mem.getGender())).toList();
    }

    @Override
    public List<MemberSearchRes> findMyBlockList(String userId) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        return member.getBlockFromList().stream()
                .map(block -> new MemberSearchRes(block.getTo().getNickname(), block.getTo().getGender())).toList();


    }
}
