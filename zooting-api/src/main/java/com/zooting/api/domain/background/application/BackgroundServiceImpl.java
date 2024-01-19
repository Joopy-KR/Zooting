package com.zooting.api.domain.background.application;

import com.zooting.api.domain.background.dao.BackgroundInventoryRepository;
import com.zooting.api.domain.background.dao.BackgroundRepository;
import com.zooting.api.domain.background.dto.request.BackgroundReq;
import com.zooting.api.domain.background.dto.response.BackgroundRes;
import com.zooting.api.domain.background.entity.Background;
import com.zooting.api.domain.background.entity.BackgroundInventory;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BackgroundServiceImpl implements BackgroundService{
    final private MemberRepository memberRepository;
    final private BackgroundRepository backgroundRepository;
    final private BackgroundInventoryRepository backgroundInventoryRepository;
    @Override
    public Boolean buyBackgroundImg(String userId, BackgroundReq backgroundReq) {
        Member member = memberRepository.findMemberByEmail(userId)
                .orElseThrow(()->new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));
        Long memberPoints = member.getPoint();
        Background background = backgroundRepository.getReferenceById(backgroundReq.backgroundId());

        // 이미 샀던거면 return false
        List<BackgroundInventory> backgroundInventories = member.getMyBackgrounds();
        for(var bgImg : backgroundInventories) {
            if (bgImg.getId() == background.getId()) {
                return false;
            }
        }
        // 가격보다 포인트가 적으면 return false
        if (memberPoints < background.getPrice()) {
            return false;
        }else {
            // 포인트 차감
            member.setPoint(memberPoints - background.getPrice());
            memberRepository.save(member);
            // 인벤토리 추가
            BackgroundInventory bgInventory = new BackgroundInventory();
            bgInventory.setBackground(background);
            bgInventory.setMember(member);
            backgroundInventoryRepository.save(bgInventory);
            return true;
        }

    }

    @Override
    public List<BackgroundRes> findAllBackgroundImg() {
        List<BackgroundRes> backgroundResList = backgroundRepository.findAll()
                .stream().map(back-> new BackgroundRes(back.getFile().getFileName(), back.getFile().getImg_url(), back.getPrice())).toList();
        return  backgroundResList;
    }

    @Override
    public List<BackgroundRes> findAllBackgroundInventory() {
        return null;
    }

//    @Override
//    public List<BackgroundRes> findAllBackgroundInventory(String userId) {
////        Member member = memberRepository.findMemberByEmail(userId);
////        List<BackgroundInventory> bgInvenList = backgroundInventoryRepository.findAllByMember()
//
//        return null;
//    }
}
