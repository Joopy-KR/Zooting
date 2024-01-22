package com.zooting.api.application.dto.response;

public record MemberAndMaskRes(
        String animal,
        String description,
        Long price,
        String fileName,
        String imgUrl
) {
}
