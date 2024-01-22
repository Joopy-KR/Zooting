package com.zooting.api.domain.mask.dto.response;

public record MaskRes(
        String animal,
        String description,
        Long price,
        String fileName,
        String imgUrl
) {
}
