package com.zooting.api.domain.game.application;

import com.zooting.api.domain.game.dao.BalanceGameRepository;
import com.zooting.api.domain.game.dao.CatchMindRepository;
import com.zooting.api.domain.game.dao.LiarGameRepository;
import com.zooting.api.domain.game.dto.response.BalanceGameRes;
import com.zooting.api.domain.game.dto.response.CatchMindRes;
import com.zooting.api.domain.game.dto.response.LiarGameRes;
import com.zooting.api.domain.game.entity.BalanceGame;
import com.zooting.api.domain.game.entity.CatchMind;
import com.zooting.api.domain.game.entity.LiarGame;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{
    final private BalanceGameRepository balanceGameRepository;
    final private CatchMindRepository catchMindRepository;
    final private LiarGameRepository liarGameRepository;
    @Override
    public BalanceGameRes findBalanceSentences() {
        BalanceGame balanceGame = balanceGameRepository.find().orElseThrow(()->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        BalanceGameRes balanceGameRes = new BalanceGameRes(balanceGame.getSentence1(), balanceGame.getSentence2());
        return balanceGameRes;
    }

    @Override
    public CatchMindRes findCatchMindWord() {
        CatchMind catchMind = catchMindRepository.find().orElseThrow(()->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        CatchMindRes catchMindRes = new CatchMindRes(catchMind.getWord());
        return catchMindRes;
    }

    @Override
    public LiarGameRes findLiarGameTopicAndWord() {
        String liarGameTopic = liarGameRepository.findTopic().orElseThrow(()->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        List<String> liarGameWordList = liarGameRepository.findWord(liarGameTopic).orElseThrow(()->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR)).stream().map(game -> game.getWord()).toList();
        LiarGameRes liarGameRes = new LiarGameRes(liarGameTopic, liarGameWordList.get(0), liarGameWordList.get(1));

        return liarGameRes;
    }

}
