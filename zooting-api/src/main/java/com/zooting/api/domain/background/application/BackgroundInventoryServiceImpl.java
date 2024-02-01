package com.zooting.api.domain.background.application;

import com.zooting.api.application.dto.response.MemberAndBackgroundRes;
import com.zooting.api.domain.background.dao.BackgroundInventoryRepository;
import com.zooting.api.domain.background.dto.response.BackgroundRes;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BackgroundInventoryServiceImpl implements BackgroundInventoryService {

    final private BackgroundInventoryRepository backgroundInventoryRepository;
    public List<BackgroundRes> findAllBackgroundInventory(String memberId) {
        return backgroundInventoryRepository.findAllByMember_Email(memberId)
                .stream().map(back-> new BackgroundRes(
                        back.getId(),
                        back.getBackground().getFile().getFileName(),
                        back.getBackground().getFile().getImgUrl(),
                        back.getBackground().getPrice()) ).toList();
    }
}
