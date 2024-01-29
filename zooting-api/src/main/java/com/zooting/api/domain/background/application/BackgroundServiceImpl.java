package com.zooting.api.domain.background.application;

import com.zooting.api.domain.background.dao.BackgroundRepository;
import com.zooting.api.domain.background.dto.response.BackgroundRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BackgroundServiceImpl implements BackgroundService{
    final private BackgroundRepository backgroundRepository;

    @Override
    public List<BackgroundRes> findAllBackgroundImg(Pageable pageable) {
        return backgroundRepository.findBackgroundsBy(pageable)
                .stream().map(back-> new BackgroundRes(back.getId(), back.getFile().getFileName(), back.getFile().getImgUrl(), back.getPrice())).toList();
    }
}
