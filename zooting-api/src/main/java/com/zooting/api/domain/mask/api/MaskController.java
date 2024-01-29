package com.zooting.api.domain.mask.api;


import com.zooting.api.domain.mask.application.MaskService;
import com.zooting.api.domain.mask.dto.response.MaskPageRes;
import com.zooting.api.domain.mask.dto.response.MaskRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/items/mask")
@RequiredArgsConstructor
@Tag(name="마스크", description = "마스크 관련 API")
public class MaskController {
    final private MaskService maskService;

    @Operation(summary = "모든 마스크 조회")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    public ResponseEntity<BaseResponse<MaskPageRes>> findMasks(
            @PageableDefault(sort="createdAt", direction = Sort.Direction.DESC, page=0) Pageable pageable,
            @RequestParam(value="animal", required=false) String animal
    )  {
        MaskPageRes result = maskService.findMask(pageable, animal);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }

}
