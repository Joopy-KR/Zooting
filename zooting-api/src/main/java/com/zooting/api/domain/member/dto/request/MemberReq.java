package com.zooting.api.domain.member.dto.request;

import java.util.Date;
import java.util.List;


public record MemberReq(
       String email,
       String nickname,
       String birth,
       String address,
       List<String> interest,
       List<String> idealAnimal
) {
}
