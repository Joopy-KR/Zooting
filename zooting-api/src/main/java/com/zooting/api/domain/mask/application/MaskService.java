package com.zooting.api.domain.mask.application;

import com.zooting.api.domain.mask.dto.response.MaskRes;

import java.util.List;


public interface MaskService {
    List<MaskRes> findAllMask();
}
