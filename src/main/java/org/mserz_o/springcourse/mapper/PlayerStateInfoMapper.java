package org.mserz_o.springcourse.mapper;

import org.mserz_o.springcourse.dto.PlayerStateInfo;
import org.mserz_o.springcourse.model.enums.Point;
import org.mserz_o.springcourse.model.enums.Side;
import org.mserz_o.springcourse.model.memory.ActiveMatch;

public class PlayerStateInfoMapper {

    public static PlayerStateInfo mapFrom(ActiveMatch activeMatch, Side side){

        return PlayerStateInfo
                .builder()
                .playerDto(PlayerMapper.mapFrom(activeMatch.getPlayer(side)))
                .set(activeMatch.getMatchScore().getPlayerSets(side).getValue())
                .game(activeMatch.getMatchScore().getSetState().getPlayerGames(side).getValue())
                .tieBreak(activeMatch.getMatchScore().getSetState().getTieBreakState().getPlayerTieBreak(side).getValue())
                .point(mapPoint(activeMatch.getMatchScore().getSetState().getGameState().getPlayerPoints(side).getValue()))
                .build();
    }

    private static String mapPoint(Point point){
        return switch (point){
            case null -> null;
            case LOVE -> "0";
            case FIFTEEN -> "15";
            case THIRTY -> "30";
            case FORTY -> "40";
            case ADVANTAGE -> "AD";
        };
    }
}
