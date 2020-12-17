package com.forcarbonit.game;

import com.forcarbonit.display.Display;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
@NonNull
public class Board {
    private static final Logger logger = Logger.getLogger(Board.class.getName());

    private int _width;

    private int _height;

    private int _playersOnBoardCount = 0;

    private int _mountainsOnBoardCount = 0;

    private HashMap<Position, CellInfo> _elemsInBoard = new HashMap<>();

    private List<Player> playerList = new ArrayList<>();

    private Display _display = new Display();

    public void countPlayer() {
        _playersOnBoardCount++;
    }

    public void countMountain() {
        _mountainsOnBoardCount++;
    }

    public void printBoard() {
        for (int j = 0; j < _height; j++) {
            for (int i = 0; i < _width; i++) {
                Position pos = new Position(i, j);
                _display.display("[");
                if (_elemsInBoard.containsKey(pos)) {
                    preciseBoard(pos);
                } else {
                    _display.display(".");
                }
                _display.display("]");
            }
            _display.display("%n");
        }
    }

    public void preciseBoard(Position pos) {
        CellInfo info = _elemsInBoard.get(pos);
        if (info.is_hasMountain())
            _display.display("M");
        else if (info.is_hasPlayer())
            _display.display("A (%s)", info.get_player().get_name());
        if (info.is_hasBounty())
            _display.display("T (%d)", info.get_bounties());
    }

    public void addElemOnBoard(Position pos, CellInfo cellInfo) {
        if (pos != null && cellInfo != null) {
            _elemsInBoard.put(pos, cellInfo);
        }
    }

    public PositionResponse getPositionResponseForAdventurer(Position position, CellInfo cellInfo) {
        if (cellInfo.is_hasPlayer() || cellInfo.is_hasMountain()) {
            return new PositionResponse(position, false);
        }
        return new PositionResponse(position, true);
    }

    // get nearest empty position from a position
    public PositionResponse getEmptyPosition(Position pos, boolean isAddable, ElementType searchedElementType) {
        logger.log(Level.INFO, "Looking for a {0} at {1}{2}", new Object[]{searchedElementType, pos.get_posX(),
                pos.get_posY()});
        HashMap<Position, Double> tmp = new HashMap<>();
        HashMap<Position, Double> tmpAddable = new HashMap<>();

        for (int i = 0; i < _width; i++) {
            for (int j = 0; j < _height; j++) {
                Double dist = Point2D.distance(pos.get_posX(), pos.get_posY(), i, j);
                Position tmpPos = new Position(i, j);
                if (!_elemsInBoard.containsKey(tmpPos)) {
                    logger.log(Level.INFO, "Available distance between {0}{1} and {2}{3} is {4}",
                            new Object[] {pos.get_posX(), pos.get_posY(), i, j, dist});
                    tmp.put(tmpPos, dist);
                } else {
                    if (isAddable) {
                        if ((searchedElementType == ElementType.ADVENTURER && _elemsInBoard.get(tmpPos).is_hasPlayer())
                        || (searchedElementType == ElementType.BOUNTY && _elemsInBoard.get(tmpPos).is_hasBounty())) {
                            tmpAddable.put(tmpPos, dist);
                        }
                    }
                }
            }
        }
        if (tmp.isEmpty()) {
            if (tmpAddable.isEmpty()) {
                //System.out.println("no addable cell");
                return null;
            }
            //System.out.println("addable cell");
            return new PositionResponse(getMinDistFromMap(tmpAddable), false);

        }
        //System.out.println("cell is free");
        return new PositionResponse(getMinDistFromMap(tmp), true);
    }

    private Position getMinDistFromMap(HashMap<Position, Double> map) {
        Position minPos = null;
        Double minDist = null;

        for (Map.Entry<Position, Double> elem : map.entrySet()) {
            if (minDist == null || elem.getValue().compareTo(minDist) > 0) {
                minDist = elem.getValue();
                minPos = elem.getKey();
            }
        }
        return new PositionResponse(minPos, true);
    }

    public Position _getPositionByDirection(Position currentPosition, char currentDirection) {
        Position newPos = new Position();
        switch (currentDirection) {
            case 'N':
                newPos.set_posX(currentPosition.get_posX());
                newPos.set_posY(currentPosition.get_posY() - 1);
                break;
            case 'S':
                newPos.set_posX(currentPosition.get_posX());
                newPos.set_posY(currentPosition.get_posY() + 1);
                break;
            case 'E':
                newPos.set_posY(currentPosition.get_posY());
                newPos.set_posX(currentPosition.get_posX() + 1);
                break;
            case 'O':
                newPos.set_posY(currentPosition.get_posY());
                newPos.set_posX(currentPosition.get_posX() - 1);
                break;
            default:
                break;
        }

        if (newPos.get_posX() < 0)
            newPos.set_posX(newPos.get_posX() + _width);
        if (newPos.get_posX() >= _width)
            newPos.set_posX(newPos.get_posX() - _width);
        if (newPos.get_posY() < 0)
            newPos.set_posY(newPos.get_posY() + _height);
        if (newPos.get_posY() >= _height)
            newPos.set_posY(newPos.get_posY() - _height);
        return newPos;
    }

    //test
/*    public void printEachCell() {
        System.out.println("print each cell present with elem=" +_elemsInBoard.size());
        for (Map.Entry<Position, CellInfo> cell : _elemsInBoard.entrySet()) {
            System.out.print("POS "+cell.getKey()._posX+""+cell.getKey()._posY);
            //System.out.println("AND IS"+cell.getKey());

            System.out.println("hasbounty="+cell.getValue().is_hasBounty()+"hasplayer="+cell.getValue().is_hasPlayer()+"hasmountain="+cell.getValue().is_hasMountain());

        }
    }*/

    public void printGamePrompt() {
        _display.display("C - %d - %d%n", _width, _height);
        for (Map.Entry<Position, CellInfo> cell : _elemsInBoard.entrySet()) {
            int x = cell.getKey().get_posX();
            int y = cell.getKey().get_posY();
            CellInfo value = cell.getValue();
            if (value.is_hasMountain()) {
                _display.display("M - %d - %d%n", x, y);
            }
            if (value.is_hasBounty()) {
                _display.display("# {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}%n");
                _display.display("T - %d - %d - %d%n", x, y, value.get_bounties());
            }
            if (value.is_hasPlayer()) {
                _display.display("# {A comme Aventurier} - {Nom de l'aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}\n");
                _display.display("A - %s - %d - %d - %c - %d%n", value.get_player().get_name(),
                        value.get_player().get_position().get_posX(), value.get_player().get_position().get_posY(),
                        value.get_player().get_position().get_orientation(), value.get_player().get_countBounty());
            }
        }
    }
}