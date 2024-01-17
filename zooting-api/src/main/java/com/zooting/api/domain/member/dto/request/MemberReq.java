package com.zooting.api.domain.member.dto.request;

import java.util.Date;
import java.util.List;


public record MemberReq(
       String email,
       String gender,
       String nickname,
       Date birth,
       String address,
       Long point,
       Boolean status,
       //additional info
       String personality,
       String animal,
       List<String> interest,
       List<String> ideal_animal,
       Long mask_id,
       Long background_id
) {
}
