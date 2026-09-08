package org.mserz_o.springcourse.model.memory.tiebreak;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mserz_o.springcourse.model.enums.Side;
import org.mserz_o.springcourse.model.enums.TieBreakResult;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TieBreakStateTest {

    private TieBreakState tieBreakState;

    @BeforeEach
    void createTieBreakState(){
        tieBreakState = new TieBreakState();
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldIncrementPointsForBothPlayers(Side winnerSide, Side loserSide){

        for (int i = 0; i < 6; i++) {
            tieBreakState.playTieBrake(winnerSide);
            TieBreakResult tieBreakResult = tieBreakState.playTieBrake(loserSide);

            assertEquals(i+1,tieBreakState.getPlayerTieBreak(winnerSide).getValue());
            assertEquals(i+1,tieBreakState.getPlayerTieBreak(loserSide).getValue());
            assertEquals(TieBreakResult.ONGOING,tieBreakResult);
        }

    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldWinTieBreakWhenPlayerReachesSevenPointsWithTwoPointAdvantage(Side winnerSide, Side loserSide){
        scoreTieBreakPoint(loserSide,5);
        scoreTieBreakPoint(winnerSide, 6);

        TieBreakResult tieBreakResult = tieBreakState.playTieBrake(winnerSide);

        assertNull(tieBreakState.getPlayerTieBreak(winnerSide).getValue());
        assertNull(tieBreakState.getPlayerTieBreak(loserSide).getValue());
        assertEquals(TieBreakResult.TIEBREAK_WIN, tieBreakResult);
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldContinueTieBreakWhenPlayerLeadsByOnePointAtSevenPoints(Side winnerSide, Side loserSide){
        scoreTieBreakPoint(winnerSide, 6);
        scoreTieBreakPoint(loserSide,6);

        TieBreakResult tieBreakResult = tieBreakState.playTieBrake(winnerSide);

        assertEquals(7,tieBreakState.getPlayerTieBreak(winnerSide).getValue());
        assertEquals(6,tieBreakState.getPlayerTieBreak(loserSide).getValue());
        assertEquals(TieBreakResult.ONGOING, tieBreakResult);
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldWinTieBreakWhenPlayerGetsTwoPointAdvantageAfterSevenPoints(Side winnerSide, Side loserSide){
        scoreTieBreakPoint(loserSide,6);
        scoreTieBreakPoint(winnerSide, 7);

        TieBreakResult tieBreakResult = tieBreakState.playTieBrake(winnerSide);

        assertNull(tieBreakState.getPlayerTieBreak(winnerSide).getValue());
        assertNull(tieBreakState.getPlayerTieBreak(loserSide).getValue());
        assertEquals(TieBreakResult.TIEBREAK_WIN, tieBreakResult);
    }

    static Stream<Arguments> getSides(){
        return Stream.of(
                Arguments.of(Side.A, Side.B),
                Arguments.of(Side.B,Side.A)
        );
    }

    private void scoreTieBreakPoint(Side side, int currentPosition){
        for (int i = 0; i < currentPosition; i++) {
            tieBreakState.playTieBrake(side);
        }
    }
}
