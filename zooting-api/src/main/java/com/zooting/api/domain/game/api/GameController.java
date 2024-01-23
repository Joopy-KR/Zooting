package com.zooting.api.domain.game.api;

import com.zooting.api.domain.game.application.GameService;
import com.zooting.api.domain.game.dto.response.BalanceGameRes;
import com.zooting.api.domain.game.dto.response.CatchMindRes;
import com.zooting.api.domain.game.dto.response.LiarGameRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    final private GameService gameService;
    @GetMapping("/{meetingId}/balance")
    public ResponseEntity<BaseResponse<BalanceGameRes>> findBalanceGameSentences(){
        BalanceGameRes balanceGameRes = gameService.findBalanceSentences();

        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                balanceGameRes
        );
    }

    @GetMapping("/{meetingId}/cmind")
    public ResponseEntity<BaseResponse<CatchMindRes>> findCatchMindWord() {
        CatchMindRes catchMindRes = gameService.findCatchMindWord();
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                catchMindRes
        );
    }


    @GetMapping("/{meetingId}/liars")
    public ResponseEntity<BaseResponse<LiarGameRes>> findLiarGame() {
        LiarGameRes liarGameRes = gameService.findLiarGameTopicAndWord();
        return BaseResponse.success(
            SuccessCode.CHECK_SUCCESS,
                liarGameRes
        );
    }
}
