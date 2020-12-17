package com.forcarbonit.game;

import lombok.Getter;

@Getter
public class Mountain extends Position {
    public Mountain(int posX, int posY) {
        super();
        _posX = posX;
        _posY = posY;
    }
}
