package com.zooting.api.domain.background.api;

import com.zooting.api.domain.background.application.BackgroundService;
import com.zooting.api.domain.background.dto.response.BackgroundRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="배경", description = "배경 관련 API")
public class BackgroundController {
    final private BackgroundService backgroundService;
    @Operation(summary = "전체 배경 이미지 조회")
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
