package org.mserz_o.springcourse.model.memory.set;

import lombok.Getter;
import org.mserz_o.springcourse.model.enums.GameResult;
import org.mserz_o.springcourse.model.enums.SetResult;
import org.mserz_o.springcourse.model.enums.Side;
import org.mserz_o.springcourse.model.enums.TieBreakResult;
import org.mserz_o.springcourse.model.memory.game.GameState;
import org.mserz_o.springcourse.model.memory.tiebreak.TieBreakState;

public class SetState {

    private final PlayerGames firstPlayerGames;
    private final PlayerGames secondPlayerGames;
    @Getter
    private final GameState gameState;
    @Getter
    private final TieBreakState tieBreakState;

    public SetState(){
        gameState = new GameState();
        tieBreakState = new TieBreakState();
        firstPlayerGames = new PlayerGames(Side.A);
        secondPlayerGames = new PlayerGames(Side.B);
    }

    public PlayerGames getPlayerGames(Side side){
        return switch (side){
            case A -> firstPlayerGames;
            case B -> secondPlayerGames;
        };
    }

    private Side getOpponent(Side side){
        return switch (side){
            case A -> Side.B;
            case B -> Side.A;
        };
    }

    private boolean isTieBreak(){
        return firstPlayerGames.getValue() == 6 && secondPlayerGames.getValue() == 6;
    }

    private boolean hasWonSet(PlayerGames winner, PlayerGames loser){
        return winner.getValue() >= 6 && (winner.getValue() - loser.getValue()) >= 2;
    }

    private SetResult setDefaultAfterSet(){
        firstPlayerGames.setDefault();
        secondPlayerGames.setDefault();
        return SetResult.SET_WIN;
    }

    public SetResult playSet(Side winnerSide){
        PlayerGames winner = getPlayerGames(winnerSide);
        PlayerGames loser = getPlayerGames(getOpponent(winnerSide));

        if(isTieBreak()){
            TieBreakResult tieBreakResult = tieBreakState.playTieBrake(winnerSide);
            if(tieBreakResult == TieBreakResult.TIEBREAK_WIN){
                return setDefaultAfterSet();
            }
            return SetResult.ONGOING;
        }

        GameResult gameResult = gameState.playGame(winnerSide);

        if(gameResult == GameResult.GAME_WIN){
            winner.addGame();
        }

        if(hasWonSet(winner,loser)){
            return setDefaultAfterSet();
        }

        return SetResult.ONGOING;
    }

}
