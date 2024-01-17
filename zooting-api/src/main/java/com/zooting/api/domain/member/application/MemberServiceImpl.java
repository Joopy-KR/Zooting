package com.zooting.api.domain.member.application;

import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.dto.request.InterestsReq;
import com.zooting.api.domain.member.dto.request.IntroduceReq;
import com.zooting.api.domain.member.dto.request.MemberReq;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.entity.AdditionalInfo;
import com.zooting.api.domain.member.entity.Member;
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

    @Override
    public boolean existNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    @Override
    public void updateMemberInfo(MemberReq memberReq) throws ParseException {
        Member member = memberRepository.findMemberByEmail(memberReq.email()).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        if (existNickname(memberReq.nickname())) {
            throw new BaseExceptionHandler(ErrorCode.NOT_VALID_ERROR); // TODO
        }
        member.setNickname(memberReq.nickname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        member.setBirth(sdf.parse(memberReq.birth()));
        member.setAddress(memberReq.address());


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
    public void updateInterestsandIdeal(InterestsReq additionalReq) {
        Member member = memberRepository.findMemberByEmail(additionalReq.email())
                .orElseThrow(()-> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        AdditionalInfo additionalInfo = member.getAdditionalInfo();
        additionalInfo.setInterest(additionalReq.interest().toString());
        additionalInfo.setIdealAnimal(additionalReq.idealAnimal().toString());
        memberRepository.save(member);


    }
    @Transactional
    @Override
    public void updateIntroduce(IntroduceReq introduceReq) {
        Member member = memberRepository.findMemberByEmail(introduceReq.email())
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        AdditionalInfo additionalInfo = member.getAdditionalInfo();
        additionalInfo.setIntroduce(introduceReq.introduce());
        memberRepository.save(member);
    }

    @Transactional
    @Override
    public List<MemberRes> findMemberList(String email, String nickname) {
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));

        // 내가 차단한 유저 리스트 추출
        List<Block> blockList = member.getBlockList();
        List<String> blockedMembers = new ArrayList<>();
        for (var block : blockList) {
            blockedMembers.add(block.getTo().getNickname());
        }
        // 나중에 공부 java stream
//        List<String> nicknames = member.getBlockList().stream().map(block -> block.getTo().getNickname()).toList();
        // 내가 차단하지 않은 유저 중 닉네임을 포함하는 유저를 검색
        List<Member> findMembers = memberRepository.findByNicknameContainingAndNicknameNotIn(nickname, blockedMembers);

        List<MemberRes> resultList = new ArrayList<>();
        for (var find : findMembers) {
            resultList.add(new MemberRes(find.getNickname(), find.getEmail()));
        }
        return resultList;
    }

}
