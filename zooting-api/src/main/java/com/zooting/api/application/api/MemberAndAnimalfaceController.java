package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.MemberAndAnimalfaceReq;
import com.zooting.api.application.usecase.MemberAndAnimalfaceUsecase;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/animalface")
@RequiredArgsConstructor
@Tag(name="멤버와 동물상", description = "멤버와 동물상 관련 API")
public class MemberAndAnimalfaceController {
    private final MemberAndAnimalfaceUsecase memberAndAnimalfaceUsecase;
    @Operation(summary = "제일 닮은 동물상, 동물상 닮은 비율 저장")
    @PostMapping
    public ResponseEntity<BaseResponse<String>> buyBackgroundImg(
            @Valid @RequestBody MemberAndAnimalfaceReq animalfaceReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberAndAnimalfaceUsecase.saveMemberAndAnimal(userDetails.getUsername(), animalfaceReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "동물상 저장 완료"
        );
    }
}
