package com.forcarbonit.game;

import com.forcarbonit.exception.GameException;
import com.forcarbonit.exception.ReaderException;

abstract class AbstractGame {
    public abstract void play(String str) throws ReaderException, GameException;
    public abstract void runGame();
}
