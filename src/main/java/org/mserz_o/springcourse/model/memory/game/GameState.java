package org.mserz_o.springcourse.model.memory.game;

import org.mserz_o.springcourse.model.enums.GameResult;
import org.mserz_o.springcourse.model.enums.Point;
import org.mserz_o.springcourse.model.enums.Side;

public class GameState {

    private final PlayerPoints firstPlayerPoints;
    private final PlayerPoints secondPlayerPoints;

    public GameState() {
        firstPlayerPoints = new PlayerPoints(Side.A);
        secondPlayerPoints = new PlayerPoints(Side.B);
    }

    private boolean isDeuce() {
        return firstPlayerPoints.getValue() == Point.FORTY
                && secondPlayerPoints.getValue() == Point.FORTY;
    }

    private boolean hasAdvantage(PlayerPoints player, PlayerPoints opponent) {
        return player.getValue() == Point.ADVANTAGE
                && opponent.getValue() == Point.FORTY;
    }

    private boolean hasWonGame(PlayerPoints winner, PlayerPoints loser) {
        return winner.getValue() == Point.FORTY
                && loser.getValue() != Point.FORTY;
    }

    public PlayerPoints getPlayerPoints(Side side) {
        return switch (side) {
            case A -> firstPlayerPoints;
            case B -> secondPlayerPoints;
        };
    }

    private Side getOpponent(Side side) {
        return switch (side) {
            case A -> Side.B;
            case B -> Side.A;
        };
    }

    private void setDefault(){
        firstPlayerPoints.setDefault();
        secondPlayerPoints.setDefault();
    }

    public GameResult playGame(Side winnerSide) {
        PlayerPoints winner = getPlayerPoints(winnerSide);
        PlayerPoints loser = getPlayerPoints(getOpponent(winnerSide));

        if (isDeuce()) {
            winner.setAdvantage();
            return GameResult.ONGOING;
        }

        if (hasAdvantage(winner, loser)) {
            setDefault();
            return GameResult.GAME_WIN;
        }

        if (hasAdvantage(loser, winner)) {
            loser.setForty();
            return GameResult.ONGOING;
        }

        if (hasWonGame(winner, loser)) {
            setDefault();
            return GameResult.GAME_WIN;
        }

        winner.addPoint();
        return GameResult.ONGOING;
    }
}