package com.zooting.api.domain.dm.api;

import com.zooting.api.domain.dm.application.DMService;
import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dm")
@RequiredArgsConstructor
public class DMController {
    DMService dmService;

    @GetMapping("")
    public ResponseEntity<BaseResponse<Long>> getDmRoomId(@RequestParam(name = "receiver") String receiver, @AuthenticationPrincipal UserDetails userDetails){
        DMRoom dmRoom = dmService.getDMRoom(userDetails.getUsername(), receiver);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                dmRoom.getId()
        );
    }
    @GetMapping("/room")
    public ResponseEntity<BaseResponse<List<DM>>> enterDmRoom(@RequestParam(name = "roomId") Long roomId){
        List<DM> dmList = dmService.getDMList(roomId);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                dmList
        );
    }
}