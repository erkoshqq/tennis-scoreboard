package org.mserz_o.springcourse.model.memory.game;

import lombok.Getter;
import org.mserz_o.springcourse.model.enums.Point;
import org.mserz_o.springcourse.model.enums.Side;

@Getter
public class PlayerPoints {
    private final Side side;
    private Point value;

    public PlayerPoints(Side side) {
        this.side = side;
        this.value = Point.LOVE;
    }

    protected void addPoint(){
        switch (value){
            case LOVE -> {
                value = Point.FIFTEEN;
            }
            case FIFTEEN -> {
                value = Point.THIRTY;
            }
            case THIRTY -> {
                value = Point.FORTY;
            }
            default -> throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    protected void setAdvantage(){
        value = Point.ADVANTAGE;
    }

    protected void setForty(){
        value = Point.FORTY;
    }

    protected void setDefault(){
        value = Point.LOVE;
    }

}
