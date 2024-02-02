package com.zooting.api.domain.mask.api;

import com.zooting.api.application.dto.response.MemberAndMaskRes;
import com.zooting.api.domain.mask.application.MaskInventoryService;
import com.zooting.api.domain.mask.dto.response.MaskRes;
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
@RequestMapping("api/items/maskInventory")
@RequiredArgsConstructor
@Tag(name="해금마스크", description = "해금마스크 관련 API")
public class MaskInventoryController {
    final private MaskInventoryService maskInventoryService;
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    @Operation(summary = "해금 마스크 조회")
    public ResponseEntity<BaseResponse<List<MaskRes>>> findAllMaskInventory(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<MaskRes> result = maskInventoryService.findAllMaskInventory(userDetails.getUsername());
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }
}
