package com.zooting.api.domain.mask.application;

import com.zooting.api.domain.mask.dao.MaskRepository;
import com.zooting.api.domain.mask.dto.response.MaskPageRes;
import com.zooting.api.domain.mask.dto.response.MaskRes;
import com.zooting.api.domain.mask.entity.Mask;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MaskServiceImpl implements MaskService{
    final private MaskRepository maskRepository;
    @Override
    public MaskPageRes findMask(Pageable pageable, String animal) {
        // 전체 조회
        if (Objects.isNull(animal)) {
            Page<Mask> result =  maskRepository.findMasksBy(pageable);
            System.out.println("####" + result);
            List<MaskRes> maskResList = result.stream().map(mask->new MaskRes(
                    mask.getId(),
                    mask.getAnimal(),
                    mask.getDescription(),
                    mask.getPrice(),
                    mask.getFile().getFileDir(),
                    mask.getFile().getFileName(),
                    mask.getFile().getImgUrl(),
                    mask.getFile().getThumbnailUrl())).toList();
            return new MaskPageRes(maskResList, pageable.getPageNumber(), result.getTotalPages());
        }
        else {
            Page<Mask> result =  maskRepository.findMasksByAnimal(animal, pageable);
            List<MaskRes> maskResList = result.stream().map(mask->new MaskRes(
                    mask.getId(),
                    mask.getAnimal(),
                    mask.getDescription(),
                    mask.getPrice(),
                    mask.getFile().getFileDir(),
                    mask.getFile().getFileName(),
                    mask.getFile().getImgUrl(),
                    mask.getFile().getThumbnailUrl())).toList();
            return new MaskPageRes(maskResList, pageable.getPageNumber(), result.getTotalPages());
        }


    }
}
