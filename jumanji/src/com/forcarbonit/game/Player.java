package com.forcarbonit.game;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class Player {
    @Getter
    @Setter
    @NonNull
    private PlayerPosition _position;

    @Getter
    @Setter
    private String _name;

    @Getter
    @Setter
    private String _moves;

    @Getter
    @Setter
    private int _countBounty = 0;

    @Getter
    @Setter
    private boolean _started = false;

    @Getter
    @Setter
    private boolean _movesFinished = false;

    public Player(String name, int posX, int posY, char orientation, String moves) {
        _name = name;
        _position = new PlayerPosition(posX, posY, orientation);
        _moves = moves;
    }

    public Player(Player currentPlayer) {
        _name = currentPlayer.get_name();
        _position = currentPlayer.get_position();
        _moves = currentPlayer.get_moves();
        _countBounty = currentPlayer.get_countBounty();
        _started = currentPlayer.is_started();
        _movesFinished = currentPlayer.is_movesFinished();
    }

    public void changeDirectionFromDirection(char move) {
        char initialOrientation = _position.get_orientation();
        char newOrientation = 0;

        if (initialOrientation == 'N') {
            if (move == 'G')
                newOrientation = 'O';
            else if (move == 'D')
                newOrientation = 'E';
        } else if (initialOrientation == 'S') {
            if (move == 'G')
                newOrientation = 'E';
            else if (move == 'D')
                newOrientation = 'O';
        } else if (initialOrientation == 'E') {
            if (move == 'G')
                newOrientation = 'N';
            else if (move == 'D')
                newOrientation = 'S';
        } else if (initialOrientation == 'O') {
            if (move == 'G')
                newOrientation = 'S';
            else if (move == 'D')
                newOrientation = 'N';
        }
        _position.set_orientation(newOrientation);
    }
}
