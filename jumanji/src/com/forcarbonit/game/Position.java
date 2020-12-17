package com.forcarbonit.game;

import lombok.Getter;
import lombok.Setter;


public class Position {
    @Getter
    @Setter
     int _posX;

    @Getter
    @Setter
     int _posY;

    public Position() {

    }

    public Position(int posX, int posY) {
        _posX = posX;
        _posY = posY;
    }

    public Position(Position position) {
        this._posX = position._posX;
        this._posY = position._posY;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }
        //
        // if (getClass() != obj.getClass()) {
            //System.out.println("classes are different");
        //    return false;
        //}
        Position other = (Position) obj;
        if (_posX == other._posX && _posY == other._posY)
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        //System.out.println("hashcode involved");
        final int prime = 31;
        int result = 1;
        result = prime * result + _posX + _posY;
        return result;
    }
}
