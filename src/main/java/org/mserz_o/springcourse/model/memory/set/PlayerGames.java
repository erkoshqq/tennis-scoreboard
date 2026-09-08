package org.mserz_o.springcourse.model.memory.set;

import lombok.Getter;
import org.mserz_o.springcourse.model.enums.Side;

@Getter
public class PlayerGames {

    private final Side side;
    private int value;

    public PlayerGames(Side side){
        this.side = side;
        value = 0;
    }

    protected void addGame(){
        value++;
    }

    protected void setDefault(){
        value = 0;
    }
}
