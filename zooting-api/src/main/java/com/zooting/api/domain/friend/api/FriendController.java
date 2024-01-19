package com.zooting.api.domain.friend.api;

import com.zooting.api.domain.friend.application.FriendService;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@PreAuthorize("hasAnyRole('ANONYMOUS')")
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {
    private FriendService friendService;
//    @PreAuthorize("hasAnyRole('USER')")
//    @GetMapping("")
//    public void test(@AuthenticationPrincipal Authentication authentication) {
//        log.info(authentication.getName());
//    }
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<FriendRes>>> findFriendList(@AuthenticationPrincipal Authentication authentication){
        log.info("enter");
        List<FriendRes> friendResList = friendService.getFriends(authentication.getName());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                friendResList
        );
    }
    @PostMapping("/accept")
    public ResponseEntity<BaseResponse<String>> acceptFriend(@RequestParam FriendReq friendReq, @AuthenticationPrincipal Authentication authentication){
        friendService.acceptFriend(friendReq, authentication);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 수락 성공"
        );
    }
}
