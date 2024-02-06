package com.zooting.api.domain.dm.api;

import com.zooting.api.domain.dm.application.DMService;
import com.zooting.api.domain.dm.dto.response.DMRoomRes;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/dm")
@RequiredArgsConstructor
@Tag(name = "DM", description = "DM 관련 API")
public class DMController {
    private final DMService dmService;

    @Operation(summary = "DM방 번호 조회")
    @GetMapping("")
    public ResponseEntity<BaseResponse<Long>> getDmRoomId(@Valid @NotNull @RequestParam(name = "receiver") String receiver, @AuthenticationPrincipal UserDetails userDetails) {
        DMRoom dmRoom = dmService.getDMRoom(userDetails.getUsername(), receiver);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                dmRoom.getId()
        );
    }

    @Operation(summary = "DM방 입장")
    @GetMapping("/room")
    public ResponseEntity<BaseResponse<DMRoomRes>> enterDmRoom(@Valid @NotNull @RequestParam(name = "receiver") String receiver, @AuthenticationPrincipal UserDetails userDetails) {
        DMRoomRes dmRoomRes = dmService.enterDMRoom(userDetails.getUsername(), receiver);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                dmRoomRes
        );
    }

    @Operation(summary = "DM방 스크롤")
    @GetMapping("/room/prev")
    public ResponseEntity<BaseResponse<DMRoomRes>> getNextDm(@Valid @NotNull @RequestParam(name = "dmRoomId") Long dmRoomId, @RequestParam(name = "cursor") Long cursor) {
        DMRoomRes dmRoomRes = dmService.getDMRoomWithCursor(dmRoomId, cursor);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                dmRoomRes
        );
    }

    @Operation(summary = "DM방 퇴장")
    @PutMapping("/room/exit")
    public ResponseEntity<BaseResponse<String>> exitDmRoom(@Valid @NotNull @RequestParam(name = "dmRoomId") Long dmRoomId, @AuthenticationPrincipal UserDetails userDetails) {
        dmService.exitDmRoom(dmRoomId, userDetails.getUsername());
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "채팅방 퇴장, 커서 갱신 성공"
        );
    }
}
