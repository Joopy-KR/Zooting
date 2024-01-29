package com.zooting.api.application.usecase;

import com.zooting.api.application.dto.request.MemberAndBlockReq;
import com.zooting.api.domain.block.dao.BlockRepository;
import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAndBlockAndFriendUsecase {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final BlockRepository blockRepository;
    @Transactional
    public void insertBlockList(String userId, MemberAndBlockReq insertBlockListReq) {

        Member me = new Member();
        me.setEmail(userId);
        // 차단할 사람
        Member blockMember = memberRepository.findMemberByNickname(insertBlockListReq.nickname())
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_USER)));

        // 친구인 경우 친구 관계 삭제
        if (friendRepository.existsByFollowerAndFollowing(me, blockMember)) {   
            friendRepository.deleteFriendByFollowerAndFollowingOrFollowingAndFollower(me, blockMember, me, blockMember);
        }
        //차단 목록 등록
        Block block = new Block();
        block.setFrom(me);
        block.setTo(blockMember);
        blockRepository.save(block);

    }
    @Transactional
    public void deleteBlock(String userId, MemberAndBlockReq blockReq) {
        Member me = new Member();
        me.setEmail(userId);
        // 차단 당한 사람
        Member blockedMember = memberRepository.findMemberByNickname(blockReq.nickname())
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        System.out.println(me.getNickname());
        System.out.println(blockedMember.getEmail());
        blockRepository.deleteBlockByFromAndTo(me, blockedMember);

    }

}
