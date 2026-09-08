package org.mserz_o.springcourse.service;

import org.mserz_o.springcourse.dto.ActiveMatchDto;
import org.mserz_o.springcourse.dto.PlayerStateInfo;
import org.mserz_o.springcourse.exception.NotFoundException;
import org.mserz_o.springcourse.mapper.PlayerStateInfoMapper;
import org.mserz_o.springcourse.model.entity.Player;
import org.mserz_o.springcourse.model.enums.MatchResult;
import org.mserz_o.springcourse.model.enums.Side;
import org.mserz_o.springcourse.model.memory.ActiveMatch;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ActiveMatchService {

    private final PlayerService playerService;
    private final MatchService matchService;

    private final ConcurrentHashMap<UUID, ActiveMatch> activeMatches = new ConcurrentHashMap<>();

    public ActiveMatchService(PlayerService playerService, MatchService matchService) {
        this.playerService = playerService;
        this.matchService = matchService;
    }

    private ActiveMatch createActiveMatch(String firstPlayerName, String secondPlayerName){

        Player firstPlayer = playerService.findOrCreatePlayer(firstPlayerName);
        Player secondPlayer = playerService.findOrCreatePlayer(secondPlayerName);

        return new ActiveMatch(firstPlayer,secondPlayer);
    }

    public UUID createMatch(String firstPlayerName, String secondPlayerName){
        ActiveMatch activeMatch = createActiveMatch(firstPlayerName, secondPlayerName);
        UUID newUUID = UUID.randomUUID();
        addActiveMatch(newUUID,activeMatch);
        return newUUID;
    }

    private void addActiveMatch(UUID uuid, ActiveMatch activeMatch){
        activeMatches.put(uuid, activeMatch);
    }

    public MatchResult playerWonPoint(UUID uuid, Side winnerSide) {
        ActiveMatch activeMatch = showActiveMatch(uuid);

        MatchResult result = activeMatch.play(winnerSide);

        if (result == MatchResult.WIN) {
            finishMatch(uuid, winnerSide);
        }

        return result;
    }

    private ActiveMatch showActiveMatch(UUID uuid) {
        ActiveMatch activeMatch = activeMatches.get(uuid);

        if (activeMatch == null) {
            throw new NotFoundException(
                    "Матч с UUID " + uuid + " не найден"
            );
        }

        return activeMatch;
    }

    public ActiveMatchDto showMatchInfo(UUID uuid){
        ActiveMatch activeMatch = showActiveMatch(uuid);
        PlayerStateInfo firstPlayerStateInfo = PlayerStateInfoMapper.mapFrom(activeMatch, Side.A);
        PlayerStateInfo secondPlayerStateInfo = PlayerStateInfoMapper.mapFrom(activeMatch, Side.B);
        return ActiveMatchDto.builder()
                .firstPlayerStateInfo(firstPlayerStateInfo)
                .secondPlayerStateInfo(secondPlayerStateInfo)
                .build();
    }

    private void saveFinishedMatch(UUID uuid, Side winnerSide){
        ActiveMatch activeMatch = showActiveMatch(uuid);
        Player winner = activeMatch.getPlayer(winnerSide);
        matchService.saveMatch(activeMatch.getFirstPlayer(),activeMatch.getSecondPlayer(),winner);
    }

    private void removeActiveMatch(UUID uuid){
        activeMatches.remove(uuid);
    }

    public void finishMatch(UUID uuid, Side winnerSide){
        saveFinishedMatch(uuid,winnerSide);
        removeActiveMatch(uuid);
    }


}
