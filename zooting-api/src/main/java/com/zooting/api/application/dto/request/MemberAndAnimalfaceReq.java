package com.zooting.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
@Schema(description = "동물상 저장시 요청 DTO")
public record MemberAndAnimalfaceReq(
        List<Long> animalfaceList
) {
}
