package com.zooting.api.domain.game.application;


import com.zooting.api.domain.game.dto.response.BalanceGameRes;
import com.zooting.api.domain.game.dto.response.CatchMindRes;
import com.zooting.api.domain.game.dto.response.LiarGameRes;

public interface GameService {
    BalanceGameRes findBalanceSentences();
    CatchMindRes findCatchMindWord();
    LiarGameRes findLiarGameTopicAndWord();
}
