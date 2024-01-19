package com.zooting.api.domain.member.dto.request;

import java.util.List;

public record InterestsReq(
        List<String> interest,
        List<String> idealAnimal
) {}
