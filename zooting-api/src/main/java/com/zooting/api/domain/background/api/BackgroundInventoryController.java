package com.zooting.api.domain.background.api;

import com.zooting.api.application.dto.response.MemberAndBackgroundRes;
import com.zooting.api.domain.background.application.BackgroundInventoryService;
import com.zooting.api.domain.background.dto.response.BackgroundRes;
import com.zooting.api.domain.background.entity.BackgroundInventory;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/items/background-inventory")
@RequiredArgsConstructor
@Tag(name="해금배경", description = "해금 배경 관련 API")
public class BackgroundInventoryController {
    final private BackgroundInventoryService backgroundInventoryService;
    @Operation(summary = "해금 배경 조회")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    public ResponseEntity<BaseResponse<List<BackgroundRes>>> findAllBackgroundInventory(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<BackgroundRes> result = backgroundInventoryService.findAllBackgroundInventory(userDetails.getUsername());
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }

}
