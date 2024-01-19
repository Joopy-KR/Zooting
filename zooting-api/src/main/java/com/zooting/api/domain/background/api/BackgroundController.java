package com.zooting.api.domain.background.api;

import com.zooting.api.domain.BaseEntity;
import com.zooting.api.domain.background.application.BackgroundService;
import com.zooting.api.domain.background.dto.request.BackgroundReq;
import com.zooting.api.domain.background.dto.response.BackgroundRes;
import com.zooting.api.domain.background.entity.Background;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/items/background")
@RequiredArgsConstructor
public class BackgroundController {
    final private BackgroundService backgroundServicee;
    @PostMapping("/")
    public ResponseEntity<BaseResponse<String>> buyBackgroundImg(
            @RequestParam(name="backgroundId") BackgroundReq backgroundReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        Boolean buyBGImg = backgroundServicee.buyBackgroundImg(userDetails.getUsername(), backgroundReq);
        if (buyBGImg) {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "구매 완료"
            );
        }else {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "구매 실패 - 잔여 포인트 부족 혹은 이미 보유한 배경이미지"
            );
        }
    }
    @GetMapping("/")
    public ResponseEntity<BaseResponse<List<BackgroundRes>>> findAllBackgroundImg() {
        List<BackgroundRes> result = backgroundServicee.findAllBackgroundImg();
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }

}
