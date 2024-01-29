package com.zooting.api.domain.background.application;

import com.zooting.api.domain.background.dao.BackgroundRepository;
import com.zooting.api.domain.background.dto.response.BackgroundPageRes;
import com.zooting.api.domain.background.dto.response.BackgroundRes;
import com.zooting.api.domain.background.entity.Background;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BackgroundServiceImpl implements BackgroundService{
    final private BackgroundRepository backgroundRepository;

    @Override
    public BackgroundPageRes findAllBackgroundImg(Pageable pageable) {
        Page<Background> result = backgroundRepository.findBackgroundsBy(pageable);
        List<BackgroundRes> backgroundResList =  result
                .stream().map(back-> new BackgroundRes(back.getId(), back.getFile().getFileName(), back.getFile().getImg_url(), back.getPrice())).toList();
        return new BackgroundPageRes(backgroundResList,pageable.getPageNumber(), result.getTotalPages());
    }
}
