package com.zooting.api.domain.background.dto.response;

import com.zooting.api.domain.file.entity.File;

public record BackgroundRes(
        String fileName,
        String imgUrl,
        Long price
) {
}
