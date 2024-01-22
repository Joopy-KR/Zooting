package com.zooting.api.domain.background.api;

import com.zooting.api.domain.background.application.BackgroundService;
import com.zooting.api.domain.background.dto.response.BackgroundRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/items/background")
@RequiredArgsConstructor
public class BackgroundController {
    final private BackgroundService backgroundService;
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/")
    public ResponseEntity<BaseResponse<List<BackgroundRes>>> findAllBackgroundImg() {
        List<BackgroundRes> result = backgroundService.findAllBackgroundImg();
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }

}
