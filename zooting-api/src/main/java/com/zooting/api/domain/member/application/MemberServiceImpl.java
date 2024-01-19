package com.zooting.api.domain.member.application;

import com.zooting.api.domain.block.dao.BlockRepository;
import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.dto.response.PointRes;
import com.zooting.api.domain.member.entity.AdditionalInfo;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.report.dao.ReportRepository;
import com.zooting.api.domain.report.entity.ReportList;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final BlockRepository blockRepository;
    private final ReportRepository reportRepository;

    @Override
    public boolean existNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    @Override
    public void updateMemberInfo(String memberId, MemberReq memberReq) throws ParseException, BaseExceptionHandler {
        Member member = memberRepository.findMemberByEmail(memberId).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        if (existNickname(memberReq.nickname())) {
            throw new BaseExceptionHandler(ErrorCode.NOT_VALID_ERROR);
        }
        member.setNickname(memberReq.nickname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        member.setBirth(sdf.parse(memberReq.birth()));
        member.setAddress(memberReq.address());
        member.setPoint(0L); // 추가 정보 저장 시 포인트 0으로 저장

        AdditionalInfo additionalInfo = member.getAdditionalInfo();
        if (Objects.isNull(additionalInfo)) {
            additionalInfo = new AdditionalInfo();
        }
        additionalInfo.setInterest(memberReq.interest().toString());
        additionalInfo.setIdealAnimal(memberReq.idealAnimal().toString());
        additionalInfo.setMember(member);

        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void updateInterestsandIdeal(String memberId, InterestsReq additionalReq) {
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

    @Transactional(readOnly = true)
    @Override
    public List<MemberRes> findMemberList(String userId, String nickname) {
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

        List<MemberRes> resultList = findMembers.stream().map(mem -> new MemberRes(mem.getNickname(), mem.getEmail())).toList();
        return resultList;
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

    @Transactional
    @Override
    public void insertBlockList(String userId, BlockReq blockReq) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_USER)));
        // 차단할 사람
        Member blockMember = memberRepository.findMemberByNickname(blockReq.nickname())
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_USER)));

        // 친구인지 확인
        boolean flag = false;
        for (var friend : member.getFriendList()) {
            if (friend.getFollowing().equals(blockMember)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            friendRepository.deleteFriendByFollowerAndFollowing(member, blockMember);
            friendRepository.deleteFriendByFollowerAndFollowing(blockMember, member);
        }
        //차단 목록 등록
        Block block = new Block();
        block.setFrom(member);
        block.setTo(blockMember);
        blockRepository.save(block);

    }

    @Transactional
    @Override
    public void deleteBlock(String userId, BlockReq blockReq) {
        // 차단한 사람
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_USER)));
        // 차단 당한 사람
        Member blockedMember = memberRepository.findMemberByNickname(blockReq.nickname())
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        blockRepository.deleteBlockByFromAndTo(member, blockedMember);

    }

    @Transactional
    @Override
    public void insertReport(String userId, ReportReq reportReq) {
        Member reportedMember = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_USER)));
        ReportList reportList = new ReportList();
        reportList.setReason(reportReq.reason());
        reportList.setDate(reportReq.date());
        reportList.setMember(reportedMember);

        reportRepository.save(reportList);
    }

    @Override
    public PointRes findPoints(String userId) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_USER)));
        PointRes pointRes = new PointRes(member.getEmail(), member.getPoint());

        return pointRes;
    }

    @Transactional
    @Override
    public Boolean deductPoints(String userId, Long price) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Long memberPoints = member.getPoint();
        if (memberPoints < price) {
            return false;
        } else {
            member.setPoint(memberPoints - price);
            memberRepository.save(member);
            return true;
        }
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(RuntimeException::new); //TODO
    }

    @Override
    public Member initialMemberRegister(String email) {
        return memberRepository.save(Member
                .builder()
                .email(email)
                .build());
    }

    @Override
    public Optional<Member> checkRegisteredMember(String email) {
        return memberRepository.findByEmail(email);
    }
}
