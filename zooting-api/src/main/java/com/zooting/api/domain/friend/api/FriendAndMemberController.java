package com.zooting.api.domain.friend.api;

import com.zooting.api.domain.friend.application.FriendRequestService;
import com.zooting.api.domain.friend.application.FriendService;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.usecase.AcceptFriendUsecase;
import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@PreAuthorize("hasAnyRole('USER')")
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendAndMemberController {

    private FriendService friendService;
    private FriendRequestService friendRequestService;
    private MemberService memberService;
    private AcceptFriendUsecase acceptFriendUsecase;


    @Operation(summary = "친구 수락", description = "로그인 한 사람 기준 요청 온 친구 수락")
    @PostMapping("/accept")
    public ResponseEntity<BaseResponse<String>> acceptFriend(@Valid @NotNull @RequestParam FriendReq friendReq, @AuthenticationPrincipal Authentication authentication){
        acceptFriendUsecase.acceptFriend(friendReq, authentication);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 수락 성공"
        );
    }
}
