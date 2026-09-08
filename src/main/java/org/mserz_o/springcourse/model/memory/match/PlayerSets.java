package org.mserz_o.springcourse.model.memory.match;

import lombok.Getter;
import org.mserz_o.springcourse.model.enums.Side;

@Getter
public class PlayerSets {
    private final Side side;
    private int value;

    public PlayerSets(Side side){
        this.side = side;
        value = 0;
    }

    protected void addSet(){
        value++;
    }
}
