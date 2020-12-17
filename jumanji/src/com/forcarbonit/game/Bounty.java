package com.forcarbonit.game;

import lombok.Getter;

public class Bounty extends Position {

    @Getter
    private int _bountyCount = 0;

    public Bounty(int posX, int posY, int bountyCount) {
        super();
        _posX = posX;
        _posY = posY;
        _bountyCount = bountyCount;
    }
}
