package org.mserz_o.springcourse.model.memory.tiebreak;

import org.mserz_o.springcourse.model.enums.Side;
import org.mserz_o.springcourse.model.enums.TieBreakResult;

public class TieBreakState {

    private final PlayerTieBreakPoints firstPlayerTieBreak;
    private final PlayerTieBreakPoints secondPlayerTieBreak;

    public TieBreakState(){
        firstPlayerTieBreak = new PlayerTieBreakPoints(Side.A);
        secondPlayerTieBreak = new PlayerTieBreakPoints(Side.B);
    }

    public PlayerTieBreakPoints getPlayerTieBreak(Side side){
        return switch (side){
            case A -> firstPlayerTieBreak;
            case B -> secondPlayerTieBreak;
        };
    }

    private Side getOpponent(Side side){
        return switch (side){
            case A -> Side.B;
            case B -> Side.A;
        };
    }

    private boolean isTieBreakOver(PlayerTieBreakPoints winner, PlayerTieBreakPoints loser){
        return winner.getValue() >= 7 && winner.getValue() - loser.getValue() >= 2;
    }

    private void setDefault(){
        firstPlayerTieBreak.setDefault();
        secondPlayerTieBreak.setDefault();
    }


    public TieBreakResult playTieBrake(Side winnerSide){
        if(firstPlayerTieBreak.getValue() == null){
            firstPlayerTieBreak.setZero();
            secondPlayerTieBreak.setZero();
        }

        PlayerTieBreakPoints winner = getPlayerTieBreak(winnerSide);
        PlayerTieBreakPoints loser = getPlayerTieBreak(getOpponent(winnerSide));

        winner.addPoint();

        if(isTieBreakOver(winner,loser)){
            setDefault();
            return TieBreakResult.TIEBREAK_WIN;
        }

        return TieBreakResult.ONGOING;
    }


}
