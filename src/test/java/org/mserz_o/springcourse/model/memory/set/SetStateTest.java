package org.mserz_o.springcourse.model.memory.set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mserz_o.springcourse.model.enums.SetResult;
import org.mserz_o.springcourse.model.enums.Side;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SetStateTest {

    private SetState setState;

    @BeforeEach
    void createSetState(){
        setState = new SetState();
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldIncrementGamesWhenPlayersWin(Side winnerSide, Side loserSide){
        for (int i = 0; i < 5; i++) {
            setState.playSet(winnerSide);
            setState.playSet(winnerSide);
            setState.playSet(winnerSide);
            setState.playSet(winnerSide);
            setState.playSet(loserSide);
            setState.playSet(loserSide);
            setState.playSet(loserSide);

            SetResult setResult = setState.playSet(loserSide);

            assertEquals(i+1, setState.getPlayerGames(winnerSide).getValue());
            assertEquals(i+1, setState.getPlayerGames(loserSide).getValue());
            assertEquals(SetResult.ONGOING, setResult);
        }
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldWinSetWhenPlayerHasSixGamesWithTwoGameAdvantage(Side winnerSide, Side loserSide){
        scoreGames(loserSide,4);
        scoreGames(winnerSide,5);

        setState.playSet(winnerSide);
        setState.playSet(winnerSide);
        setState.playSet(winnerSide);
        SetResult setResult = setState.playSet(winnerSide);

        assertEquals(0,setState.getPlayerGames(winnerSide).getValue());
        assertEquals(0,setState.getPlayerGames(loserSide).getValue());
        assertEquals(SetResult.SET_WIN, setResult);
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldStartTiebreakWhenPlayersHaveSixGamesEach(Side winnerSide, Side loserSide){
        scoreGames(loserSide,5);
        scoreGames(winnerSide,6);
        scoreGames(loserSide,1);

        SetResult setResult = setState.playSet(winnerSide);

        assertEquals(6,setState.getPlayerGames(winnerSide).getValue());
        assertEquals(6,setState.getPlayerGames(loserSide).getValue());
        assertEquals(1,setState.getTieBreakState().getPlayerTieBreak(winnerSide).getValue());
        assertEquals(0,setState.getTieBreakState().getPlayerTieBreak(loserSide).getValue());
        assertEquals(SetResult.ONGOING, setResult);
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldWinSetWhenPlayerWinsTiebreak(Side winnerSide, Side loserSide){
        scoreGames(loserSide,5);
        scoreGames(winnerSide,6);
        scoreGames(loserSide,1);

        setState.playSet(winnerSide);
        setState.playSet(winnerSide);
        setState.playSet(winnerSide);
        setState.playSet(winnerSide);
        setState.playSet(winnerSide);
        setState.playSet(winnerSide);

        SetResult setResult = setState.playSet(winnerSide);

        assertEquals(0,setState.getPlayerGames(winnerSide).getValue());
        assertEquals(0,setState.getPlayerGames(loserSide).getValue());
        assertEquals(SetResult.SET_WIN, setResult);
    }

    static Stream<Arguments> getSides(){
        return Stream.of(
                Arguments.of(Side.A, Side.B),
                Arguments.of(Side.B,Side.A)
        );
    }

    private void scoreGames(Side side, int currentPosition){
        for (int i = 0; i < currentPosition; i++) {
            for (int j = 0; j < 4; j++) {
                setState.playSet(side);
            }
        }
    }
}
