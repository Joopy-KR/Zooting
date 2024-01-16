package com.zooting.api.domain.member.dto.request;

import java.util.Date;


public record MemberAdditionalInfoReq(
       String email,
       String gender,
       String nickname,
       Date birth,
       String address,
       Long point,
       Boolean status
) {
}
