package com.zooting.api.domain.member.dto.request;

import java.util.List;

public record InterestsReq(
        String email,
        List<String> interest,
        List<String> idealAnimal
) {}
