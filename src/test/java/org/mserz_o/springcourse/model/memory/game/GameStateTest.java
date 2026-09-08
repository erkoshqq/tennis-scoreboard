package org.mserz_o.springcourse.model.memory.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mserz_o.springcourse.model.enums.GameResult;
import org.mserz_o.springcourse.model.enums.Point;
import org.mserz_o.springcourse.model.enums.Side;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    private GameState gameState;

    @BeforeEach
    void createGameState(){
        gameState = new GameState();
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldFollowCorrectTennisPointProgression(Side winnerSide, Side loserSide){
        assertAll(
                ()-> assertEquals(Point.LOVE, gameState.getPlayerPoints(winnerSide).getValue()),
                ()-> {gameState.playGame(winnerSide);
                    assertEquals(Point.FIFTEEN, gameState.getPlayerPoints(winnerSide).getValue());
                },
                () -> {gameState.playGame(winnerSide);
                    assertEquals(Point.THIRTY, gameState.getPlayerPoints(winnerSide).getValue());
                },
                () -> {gameState.playGame(winnerSide);
                    assertEquals(Point.FORTY, gameState.getPlayerPoints(winnerSide).getValue());
                },
                ()-> assertEquals(Point.LOVE, gameState.getPlayerPoints(loserSide).getValue()),
                ()-> {gameState.playGame(loserSide);
                    assertEquals(Point.FIFTEEN, gameState.getPlayerPoints(loserSide).getValue());
                },
                () -> {gameState.playGame(loserSide);
                    assertEquals(Point.THIRTY, gameState.getPlayerPoints(loserSide).getValue());
                },
                () -> {gameState.playGame(loserSide);
                    assertEquals(Point.FORTY, gameState.getPlayerPoints(loserSide).getValue());
                }
        );
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldWinGameWhenPlayerWinsPoint(Side winnerSide, Side loserSide){
        scorePoints(winnerSide,3);
        scorePoints(loserSide,2);

        GameResult gameResult = gameState.playGame(winnerSide);

        assertEquals(Point.LOVE,gameState.getPlayerPoints(winnerSide).getValue());
        assertEquals(Point.LOVE,gameState.getPlayerPoints(loserSide).getValue());
        assertEquals(GameResult.GAME_WIN, gameResult);
    }


    @ParameterizedTest
    @MethodSource("getSides")
    void shouldGiveAdvantageWhenPlayerWinsPointInDeuce(Side winnerSide, Side loserSide){
        scorePoints(winnerSide, 3);
        scorePoints(loserSide, 3);

        GameResult gameResult = gameState.playGame(winnerSide);

        assertEquals(Point.ADVANTAGE,gameState.getPlayerPoints(winnerSide).getValue());
        assertEquals(GameResult.ONGOING,gameResult);
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldWinGameWhenPlayerWinsPointWithAdvantage(Side winnerSide,Side loserSide){
        scorePoints(loserSide, 3);
        scorePoints(winnerSide, 4);

        GameResult gameResult = gameState.playGame(winnerSide);

        assertEquals(Point.LOVE,gameState.getPlayerPoints(winnerSide).getValue());
        assertEquals(Point.LOVE,gameState.getPlayerPoints(loserSide).getValue());
        assertEquals(GameResult.GAME_WIN, gameResult);
    }

    @ParameterizedTest
    @MethodSource("getSides")
    void shouldReturnToDeuceWhenPlayerWinsPoint(Side winnerSide, Side loserSide){
        scorePoints(winnerSide, 3);
        scorePoints(loserSide, 4);

        GameResult gameResult = gameState.playGame(winnerSide);

        assertEquals(Point.FORTY,gameState.getPlayerPoints(winnerSide).getValue());
        assertEquals(Point.FORTY,gameState.getPlayerPoints(loserSide).getValue());
        assertEquals(GameResult.ONGOING,gameResult);
    }

    static Stream<Arguments> getSides(){
        return Stream.of(
                Arguments.of(Side.A, Side.B),
                Arguments.of(Side.B,Side.A)
        );
    }

    private void scorePoints(Side winnerSide, int currentPointPosition){
        for (int i = 0; i < currentPointPosition; i++) {
            gameState.playGame(winnerSide);
        }
    }

}
