package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.entity.Member;

import java.util.Date;
import java.util.Optional;

public interface MemberService {
    boolean existNickname(String nickname);
    void saveAdditionalInfo(String email, String gender, String nickname, Date birth, String address);
}
