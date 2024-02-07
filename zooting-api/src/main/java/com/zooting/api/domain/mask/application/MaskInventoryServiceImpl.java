package com.zooting.api.domain.mask.application;

import com.zooting.api.application.dto.response.MemberAndMaskRes;
import com.zooting.api.domain.mask.dao.MaskInventoryRepository;
import com.zooting.api.domain.mask.dto.response.MaskRes;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaskInventoryServiceImpl implements MaskInventoryService{
   final private MaskInventoryRepository maskInventoryRepository;

    @Transactional
    public List<MaskRes> findAllMaskInventory(String memberId) {
        return maskInventoryRepository.findAllByMember_Email(memberId)
                .stream().map(myMask-> new MaskRes(
                        myMask.getMask().getId(),
                        myMask.getMask().getAnimal(),
                        myMask.getMask().getDescription(),
                        myMask.getMask().getPrice(),
                        myMask.getMask().getFile().getFileDir(),
                        myMask.getMask().getFile().getFileName(),
                        myMask.getMask().getFile().getImgUrl(),
                        myMask.getMask().getFile().getThumbnailUrl()
                )).toList();
    }
}
