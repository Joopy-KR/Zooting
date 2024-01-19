package com.zooting.api.domain.friend.api;

import com.zooting.api.domain.friend.application.FriendRequestService;
import com.zooting.api.domain.friend.application.FriendService;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
//@PreAuthorize("hasAnyRole('USER')")
@RestController
@RequestMapping("/api/friends/request")
@RequiredArgsConstructor
public class FriendRequestController {
    private FriendRequestService friendRequestService;
//    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("")
    public void test(@AuthenticationPrincipal Authentication authentication) {
        log.info(authentication.getName());
    }
    @GetMapping("/from")
    public ResponseEntity<BaseResponse<List<FriendRes>>> getReceivedFriendRequests(@AuthenticationPrincipal Authentication authentication){
        List<FriendRes> friendResList = friendRequestService.getReceivedFriendRequests(authentication.getName());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                friendResList
        );
    }

    @GetMapping("/to")
    public ResponseEntity<BaseResponse<List<FriendRes>>> getSentFriendRequests(@AuthenticationPrincipal Authentication authentication){
        List<FriendRes> friendResList = friendRequestService.getSentFriendRequests(authentication.getName());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                friendResList
        );
    }

}
