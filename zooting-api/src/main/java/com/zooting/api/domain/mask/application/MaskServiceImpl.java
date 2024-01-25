package com.zooting.api.domain.mask.application;

import com.zooting.api.domain.mask.dao.MaskRepository;
import com.zooting.api.domain.mask.dto.response.MaskRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaskServiceImpl implements MaskService{
    final private MaskRepository maskRepository;
    @Override
    public List<MaskRes> findAllMask() {
        return maskRepository.findAll()
                .stream().map(mask->new MaskRes(
                        mask.getId(),
                        mask.getAnimal(),
                        mask.getDescription(),
                        mask.getPrice(),
                        mask.getFile().getFileName(),
                        mask.getFile().getImg_url())).toList();
    }
}
