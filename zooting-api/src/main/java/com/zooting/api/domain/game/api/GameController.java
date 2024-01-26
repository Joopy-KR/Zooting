package com.zooting.api.domain.game.api;

import com.zooting.api.domain.game.application.GameService;
import com.zooting.api.domain.game.dto.response.BalanceGameRes;
import com.zooting.api.domain.game.dto.response.CatchMindRes;
import com.zooting.api.domain.game.dto.response.LiarGameRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@Tag(name= "게임", description = "Game 관련 API")
public class GameController {
    final private GameService gameService;
    @GetMapping("/{meetingId}/balance")
    @Operation(summary = "밸런스게임 문장 2개 추출")
    public ResponseEntity<BaseResponse<BalanceGameRes>> findBalanceGameSentences(@PathVariable Long meetingId){
        BalanceGameRes balanceGameRes = gameService.findBalanceSentences();

        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                balanceGameRes
        );
    }

    @GetMapping("/{meetingId}/cmind")
    @Operation(summary = "캐치마인드 랜덤 단어 1개 추출")
    public ResponseEntity<BaseResponse<CatchMindRes>> findCatchMindWord(@PathVariable Long meetingId) {
        CatchMindRes catchMindRes = gameService.findCatchMindWord();
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                catchMindRes
        );
    }


    @GetMapping("/{meetingId}/liars")
    @Operation(
            summary = "라이어게임",
            description = "라이어게임 주제, 라이어가 받는 단어, 나머지가 받는 단어 총 3개 추출")
    public ResponseEntity<BaseResponse<LiarGameRes>> findLiarGame(@PathVariable Long meetingId) {
        LiarGameRes liarGameRes = gameService.findLiarGameTopicAndWord();
        return BaseResponse.success(
            SuccessCode.CHECK_SUCCESS,
                liarGameRes
        );
    }
}
