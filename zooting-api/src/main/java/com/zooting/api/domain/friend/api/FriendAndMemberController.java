package com.zooting.api.domain.friend.api;

import com.zooting.api.domain.friend.application.FriendService;
import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@PreAuthorize("hasAnyRole('USER')")
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendAndMemberController {

    private FriendService friendService;
    private MemberService memberService;

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<MemberRes>>> searchFriend(@RequestParam String nickname){
        List<MemberRes> friendSearchList = friendService.searchFriend(nickname);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                friendSearchList
        );
    }
}
