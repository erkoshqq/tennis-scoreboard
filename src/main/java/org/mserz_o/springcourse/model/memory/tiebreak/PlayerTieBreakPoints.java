package org.mserz_o.springcourse.model.memory.tiebreak;

import lombok.Getter;
import org.mserz_o.springcourse.model.enums.Side;

@Getter
public class PlayerTieBreakPoints {
    private final Side side;
    private Integer value;

    public PlayerTieBreakPoints(Side side){
        this.side = side;
        this.value = null;
    }

    protected void addPoint(){
        if(value == null){
            value = 0;
        }
        value++;
    }

    protected void setZero(){
        value = 0;
    }

    protected void setDefault(){
        value = null;
    }

}
