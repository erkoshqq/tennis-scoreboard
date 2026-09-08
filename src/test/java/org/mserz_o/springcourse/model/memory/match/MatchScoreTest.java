package org.mserz_o.springcourse.model.memory.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mserz_o.springcourse.model.enums.MatchResult;
import org.mserz_o.springcourse.model.enums.Side;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchScoreTest {

    private MatchScore matchScore;

    @BeforeEach
    void createMatchScore(){
        matchScore = new MatchScore();
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldContinueMatchAfterPlayersWinOneSet(Side winnerSide, Side loserSide){
        scoreSet(winnerSide);
        scoreSet(loserSide);
        MatchResult matchResult = matchScore.play(winnerSide);
        assertEquals(1, matchScore.getPlayerSets(winnerSide).getValue());
        assertEquals(1, matchScore.getPlayerSets(loserSide).getValue());
        assertEquals(MatchResult.ONGOING, matchResult);
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldWinMatchWhenPlayerHasTwoSetsAndAnotherHasOneSet(Side winnerSide, Side loserSide){
        scoreSet(winnerSide);
        scoreSet(loserSide);
        for (int i = 0; i < 23; i++) {
            matchScore.play(winnerSide);
        }

        MatchResult matchResult = matchScore.play(winnerSide);

        assertEquals(2,matchScore.getPlayerSets(winnerSide).getValue());
        assertEquals(1,matchScore.getPlayerSets(loserSide).getValue());
        assertEquals(MatchResult.WIN, matchResult);
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldWinMatchWhenPlayerHasTwoSetsAndAnotherHasZeroSet(Side winnerSide, Side loserSide){
        scoreSet(winnerSide);
        for (int i = 0; i < 23; i++) {
            matchScore.play(winnerSide);
        }

        MatchResult matchResult = matchScore.play(winnerSide);

        assertEquals(2,matchScore.getPlayerSets(winnerSide).getValue());
        assertEquals(0,matchScore.getPlayerSets(loserSide).getValue());
        assertEquals(MatchResult.WIN, matchResult);
    }


    private static Stream<Arguments> getSides(){
        return Stream.of(
                Arguments.of(Side.A, Side.B),
                Arguments.of(Side.B, Side.A)
        );
    }

    private void scoreSet(Side side){
        for (int i = 0; i < 24; i++) {
            matchScore.play(side);
        }
    }
}
