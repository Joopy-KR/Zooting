package com.zooting.api.domain.friend.api;

import com.zooting.api.domain.friend.application.FriendService;
import com.zooting.api.domain.friend.dto.response.FriendRes;
import com.zooting.api.domain.friend.dto.response.FriendSearchPageRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@PreAuthorize("hasAnyRole('USER')")
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@Tag(name="친구", description = "친구 관련 API")
public class FriendController {
    private final FriendService friendService;
    @Operation(summary = "친구 리스트", description = "로그인 한 사람 기준 친구리스트 반환")
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<FriendRes>>> findFriendList(@AuthenticationPrincipal UserDetails userDetails){
        log.info("enter");
        List<FriendRes> friendResList = friendService.getFriends(userDetails.getUsername());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                friendResList
        );
    }
    @Operation(summary = "친구 검색", description = "페이징 정보 포함 친구 조회")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<FriendSearchPageRes>> searchFriend(
            @PageableDefault(sort="id", direction = Sort.Direction.DESC, page=0) Pageable pageable,
            @Valid @NotNull @RequestParam String nickname, @AuthenticationPrincipal UserDetails userDetails){
        FriendSearchPageRes friendSearchList = friendService.searchFriend(pageable, nickname, userDetails.getUsername());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                friendSearchList
        );
    }
}
