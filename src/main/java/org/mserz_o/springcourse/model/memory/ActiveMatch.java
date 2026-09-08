package org.mserz_o.springcourse.model.memory;

import lombok.Getter;
import org.mserz_o.springcourse.model.entity.Player;
import org.mserz_o.springcourse.model.enums.MatchResult;
import org.mserz_o.springcourse.model.enums.Side;
import org.mserz_o.springcourse.model.memory.match.MatchScore;

@Getter
public class ActiveMatch {

    private final Player firstPlayer;
    private final Player secondPlayer;
    private final MatchScore matchScore;

    public ActiveMatch(Player firstPlayer, Player secondPlayer){
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        matchScore = new MatchScore();
    }

    public Player getPlayer(Side side){
        return switch (side){
            case A -> firstPlayer;
            case B -> secondPlayer;
        };
    }

    public synchronized MatchResult play(Side winnerSide){
        return matchScore.play(winnerSide);
    }


}
