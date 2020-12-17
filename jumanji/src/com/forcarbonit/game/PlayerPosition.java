package com.forcarbonit.game;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class PlayerPosition extends Position{
    @Getter
    @Setter
    @NonNull
    private char _orientation;

    public PlayerPosition(int posX, int posY, char startingDirection) {
        super();
        _posX = posX;
        _posY = posY;
        _orientation = startingDirection;
    }

    public void setPlayerPosition(int posX, int posY, char orientation) {
        _posX = posX;
        _posY = posY;
        _orientation = orientation;
    }

    public void setPlayerXandY(int x, int y) {
        _posX = x;
        _posY = y;
    }
}
