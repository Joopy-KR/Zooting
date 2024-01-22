package com.zooting.api.application.usecase;

import com.zooting.api.domain.friend.dao.FriendRepository;
import com.zooting.api.domain.friend.dao.FriendRequestRepository;
import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Log4j2
@PreAuthorize("hasAnyRole('USER')")
@Service
@RequiredArgsConstructor
public class SendFriendUsecase {
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;

    public void sendFriendRequest(String requestFrom, String requestTo) {
        Member fromMember = Member.builder().email(requestFrom).build();
        Member toMember = memberRepository.findByEmail(requestTo)
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        FriendRequest friendRequest = new FriendRequest(fromMember, toMember);
        friendRequestRepository.save(friendRequest);
    }
}
