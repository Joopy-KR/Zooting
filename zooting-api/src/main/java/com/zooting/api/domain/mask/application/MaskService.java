package com.zooting.api.domain.mask.application;

import com.zooting.api.domain.mask.dto.response.MaskPageRes;
import com.zooting.api.domain.mask.dto.response.MaskRes;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface MaskService {
    MaskPageRes findMask(Pageable pageable, String animal);
}
