package com.forcarbonit.check;

import com.forcarbonit.game.Position;

public class Check {
    public boolean isElementPosIsValid(int boardX, int boardY, Position toCompare) {
        if (toCompare.get_posX() >= 0 && toCompare.get_posX() <= boardX - 1
        && toCompare.get_posY() >= 0 && toCompare.get_posY() <= boardY -1) {
            return true;
        }
        return false;
    }

    public boolean isEnoughSpaceOnBoard(int width, int height, int playersOnBoardCount, int mountainsOnBoardCount) {
        return playersOnBoardCount + mountainsOnBoardCount <= width * height;
    }
}
