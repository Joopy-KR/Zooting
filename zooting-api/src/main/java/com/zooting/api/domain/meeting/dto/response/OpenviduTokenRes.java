package com.zooting.api.domain.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(name = "Openvidu Token Response", description = "Openvidu 토큰 응답")
public record OpenviduTokenRes(
        @Schema(description = "Openvidu 토큰")
        String token,
        @Schema(description = "참가자들 닉네임 리스트")
        List<String> nicknameList,
        @Schema(description = "참가자들 성별 리스트")
        List<String> genderList
) {
}
