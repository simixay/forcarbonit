package com.forcarbonit.game;

import lombok.Getter;

public class PositionResponse extends Position{
    @Getter
    private boolean _isFreeCell;
    public PositionResponse(Position minPos, boolean isFreeCell) {
        super();
        _posX = minPos._posX;
        _posY = minPos._posY;
        _isFreeCell = isFreeCell;
    }
}
