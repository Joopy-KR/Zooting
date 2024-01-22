package com.zooting.api.application.dto.response;

public record MemberAndBackgroundRes(
        String fileName,
        String imgUrl,
        Long price
) {
}
