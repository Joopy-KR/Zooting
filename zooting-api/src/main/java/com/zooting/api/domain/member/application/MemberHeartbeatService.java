package com.zooting.api.domain.member.application;

import com.zooting.api.domain.member.dto.request.HeartBeatReq;
import com.zooting.api.domain.member.dto.response.HeartBeatRes;
import com.zooting.api.global.common.SocketBaseDtoRes;

import java.util.List;

public interface MemberHeartbeatService {
    SocketBaseDtoRes<List<HeartBeatRes>> updateHeartbeatStatus(HeartBeatReq heartBeatReq);
    List<String> checkAllMemberOnline();
}
