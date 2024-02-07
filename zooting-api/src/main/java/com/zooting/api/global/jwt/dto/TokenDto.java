package com.zooting.api.global.jwt.dto;


public record TokenDto(
        String accessToken,
        String refreshToken
) {
}
