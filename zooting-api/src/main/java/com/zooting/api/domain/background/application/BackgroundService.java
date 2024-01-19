package com.zooting.api.domain.background.application;

import com.zooting.api.domain.background.dto.request.BackgroundReq;
import com.zooting.api.domain.background.dto.response.BackgroundRes;

import java.util.List;

public interface BackgroundService {
    Boolean buyBackgroundImg(String userId, BackgroundReq backgroundReq);
    List<BackgroundRes> findAllBackgroundImg();
    List<BackgroundRes> findAllBackgroundInventory();
}
