package com.zooting.api.domain.background.application;

import com.zooting.api.domain.background.dto.response.BackgroundRes;

import java.util.List;

public interface BackgroundInventoryService {
    List<BackgroundRes> findAllBackgroundInventory(String memberId);
}
