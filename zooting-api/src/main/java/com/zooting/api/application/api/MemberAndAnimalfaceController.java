package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.MemberAndAnimalfaceReq;
import com.zooting.api.application.usecase.MemberAndAnimalfaceUsecase;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import com.zooting.api.global.jwt.dto.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/animalface")
@RequiredArgsConstructor
@Tag(name = "멤버와 동물상", description = "멤버와 동물상 관련 API")
public class MemberAndAnimalfaceController {
    private final MemberAndAnimalfaceUsecase memberAndAnimalfaceUsecase;

    @Operation(
            summary = "제일 닮은 동물상, 동물상 닮은 비율 저장",
            description = "user role이 ANONYMOUS인 경우, 동물상 저장 후 role USER로 변경" +
                    "user role이 USER인 경우, 잔여 포인트 확인 후 동물상 변경")
    @PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")
    @PostMapping
    public ResponseEntity<BaseResponse<TokenDto>> saveMyAnimal(
            @Valid @RequestBody MemberAndAnimalfaceReq animalfaceReq,
            @AuthenticationPrincipal UserDetails userDetails) {

        TokenDto tokenDto = memberAndAnimalfaceUsecase.saveMemberAndAnimal(userDetails, animalfaceReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                tokenDto
        );

    }
}
