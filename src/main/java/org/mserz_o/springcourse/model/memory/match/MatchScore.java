package org.mserz_o.springcourse.model.memory.match;

import lombok.Getter;
import org.mserz_o.springcourse.model.enums.MatchResult;
import org.mserz_o.springcourse.model.enums.SetResult;
import org.mserz_o.springcourse.model.enums.Side;
import org.mserz_o.springcourse.model.memory.set.SetState;

public class MatchScore {
    private final PlayerSets firstPlayerSets;
    private final PlayerSets secondPlayerSets;
    @Getter
    private final SetState setState;

    public MatchScore(){
        setState = new SetState();
        firstPlayerSets = new PlayerSets(Side.A);
        secondPlayerSets = new PlayerSets(Side.B);
    }

    public PlayerSets getPlayerSets(Side side){
        return switch(side){
            case A -> firstPlayerSets;
            case B -> secondPlayerSets;
        };
    }

    private boolean isMatchWon(PlayerSets playerSets){
        return playerSets.getValue() == 2;
    }

    public MatchResult play(Side winnerSide){
        PlayerSets winner = getPlayerSets(winnerSide);
        SetResult setResult = setState.playSet(winnerSide);

        if(setResult == SetResult.SET_WIN){
            winner.addSet();
            if(isMatchWon(winner)){
                return MatchResult.WIN;
            }
            return MatchResult.ONGOING;
        }

        return MatchResult.ONGOING;
    }

}
