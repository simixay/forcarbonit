package com.forcarbonit.game;

import com.forcarbonit.check.Check;
import com.forcarbonit.exception.GameException;
import com.forcarbonit.exception.ReaderException;
import com.forcarbonit.reader.Reader;
import lombok.Getter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JumanjiGame extends AbstractGame {
    private static final Logger logger = Logger.getLogger(JumanjiGame.class.getName());
    @Getter
    Board _board = new Board();
    Reader _reader = new Reader();
    Check _check = new Check();

    Position tmpPosition = new Position();

    boolean gameFinished = false;
    int round = 1;

    @Override
    public void play(String path) throws ReaderException, GameException {
        _board.get_display().display("Starting game...%n");
        try {
            _reader.loadFile(path);
            loadData();
            runGame();
        } catch (ReaderException re) {
            logger.log(Level.SEVERE, "Parsing error: {0}", re.getMessage());
            throw new ReaderException(re.getMessage());
        } catch (GameException ge) {
            logger.log(Level.SEVERE, "Game error: {0}", ge.getMessage());
            throw new GameException(ge.getMessage());
        }

    }

    @Override
    public void runGame() {
        while (!gameFinished) {
            _board.get_display().display("Round %d%n", round);
            logger.log(Level.INFO, "round {0}", round);
            _board.printBoard();
            _board.get_display().display("BEFORE%n");
            for (Player currentPlayer: _board.getPlayerList()) {
                if (!currentPlayer.is_movesFinished()) {
                    PlayerPosition playerPosition = currentPlayer.get_position();
                    CellInfo cellInfo = _board.get_elemsInBoard().get(playerPosition);
                    processMoves(playerPosition, currentPlayer, cellInfo);
                }
            }
            _board.get_display().display("END ROUND%n");
            _board.get_display().display("AFTER%n");
            _board.printBoard();
            _board.get_display().display("%n");
            round++;
            checkIfGameIsFinished();
        }
        _board.printGamePrompt();
    }

    private void checkIfGameIsFinished() {
        boolean isFinished = true;
        for (Player currentPlayer : _board.getPlayerList()) {
            if (!currentPlayer.is_movesFinished())
                isFinished = false;
        }
        gameFinished = isFinished;
    }

    private void processMoves(Position currentPosition, Player currentPlayer, CellInfo currentInfo) {
        Position cloneCurrentPosition = new Position(currentPosition);
        PositionResponse newPosition = makeMove(cloneCurrentPosition, currentPlayer, true);

        if (currentPlayer.get_moves().isEmpty())
            currentPlayer.set_movesFinished(true);
        if (newPosition != null) {

            updateOldInfo(currentInfo, cloneCurrentPosition, currentPlayer);
            updateNewInfo(newPosition, currentPlayer);
            currentPlayer.set_started(true);
        }
    }

    private void updateNewInfo(PositionResponse newPosition, Player currentPlayer) {
        if (_board.get_elemsInBoard().containsKey(newPosition)) { //update
            CellInfo cellInfo = _board.get_elemsInBoard().get(newPosition);

            if (cellInfo.is_hasBounty()) {
                addBounty(currentPlayer, cellInfo);
                if (cellInfo.get_bounties() == 0)
                    cellInfo.set_hasBounty(false);
            }
            cellInfo.setAdventurer(currentPlayer);
            _board.get_elemsInBoard().replace(newPosition, cellInfo);
        } else { //
            CellInfo cellInfo = new CellInfo();
            cellInfo.setAdventurer(currentPlayer);
            _board.addElemOnBoard(newPosition, cellInfo);
        }
    }

    private void updateOldInfo(CellInfo currentInfo, Position currentPosition, Player currentPlayer) {
        _board.get_elemsInBoard().remove(currentPosition);

        if (currentInfo.is_hasBounty() && !currentPlayer.is_started())
            addBounty(currentPlayer, currentInfo);
        currentInfo.set_player(null);
        currentInfo.set_hasPlayer(false);
        if (currentInfo.is_hasBounty())
            _board.addElemOnBoard(currentPosition, currentInfo);
    }

    private PositionResponse makeMove(Position currentPosition, Player currentPlayer, boolean firstTime) {
        String playerMoves = currentPlayer.get_moves();

        int index = 1;
        for (char move : playerMoves.toCharArray()) {
            logger.log(Level.INFO, "CHAR = {0} FROM {1}", new Object[] {move, playerMoves});
            if (move == 'A') {
                Position posToReach = _board._getPositionByDirection(currentPosition,
                        currentPlayer.get_position().get_orientation());
                if (_board.get_elemsInBoard().containsKey(posToReach)) { // contains either mountain or player or bounty
                    CellInfo cellInfo = _board.get_elemsInBoard().get(posToReach);
                    PositionResponse positionResponse = _board.getPositionResponseForAdventurer(posToReach, cellInfo);
                    if (!positionResponse.is_isFreeCell()){ //&& firstTime) {
                        String newSequence = "";
                        if (currentPlayer.get_moves().length() > index)
                            newSequence = currentPlayer.get_moves().substring(index);
                        logger.log(Level.INFO,
                                "Cant go forward, blocking element {0}{1}->{2}{3} continuing sequence {4}",
                                new Object[]{currentPosition.get_posX(), currentPosition.get_posY(),
                                posToReach.get_posX(), posToReach.get_posY()});
                        currentPlayer.set_moves(newSequence);
                        return makeMove(currentPosition, currentPlayer,false);
                    } else {
                        PlayerPosition playerPosition = currentPlayer.get_position();

                        playerPosition.setPlayerXandY(positionResponse.get_posX(), positionResponse.get_posY());
                        currentPlayer.set_position(playerPosition);

                        playerMoves = playerMoves.substring(1);
                        currentPlayer.set_moves(playerMoves);

                        logger.log(Level.INFO, "Direction B{0} {1}{2}-> {3}{4}, remaining moves : {5}",
                                new Object[]{playerPosition.get_orientation(), playerPosition.get_posX(),
                                        playerPosition.get_posY(), posToReach.get_posX(), posToReach.get_posY(),
                                        playerMoves});
                        return positionResponse;
                    }
                } else {
                    logger.log(Level.INFO, "Next position has no mountain or player, go FORWARD");

                    PlayerPosition playerPosition = currentPlayer.get_position();
                    playerMoves = playerMoves.substring(1);
                    logger.log(Level.INFO, "Direction P{0} {1}{2}-> {3}{4}, remaining moves : {5}",
                            new Object[]{playerPosition.get_orientation(), playerPosition.get_posX(),
                                    playerPosition.get_posY(), posToReach.get_posX(), posToReach.get_posY(), playerMoves});
                    playerPosition.setPlayerXandY(posToReach.get_posX(), posToReach.get_posY());

                    currentPlayer.set_position(playerPosition);

                    currentPlayer.set_moves(playerMoves);

                    return new PositionResponse(posToReach, true);
                }
            } else {
                playerMoves = playerMoves.substring(1);
                logger.log(Level.INFO, "CHANGE DIRECTION {0}->{1}, remaining moves : {2}",
                        new Object[]{currentPlayer.get_position().get_orientation(), move, playerMoves});
                currentPlayer.changeDirectionFromDirection(move);
                currentPlayer.set_moves(playerMoves);
            }
            index++;
        }

        return null;
    }

    private void addBounty(Player currentPlayer, CellInfo currentInfo) {
        if (currentInfo.get_bounties() > 0) {
            currentPlayer.set_countBounty(currentPlayer.get_countBounty() + 1);
            currentInfo.set_bounties(currentInfo.get_bounties() - 1);
        }
        if (currentInfo.get_bounties() == 0)
            currentInfo.set_hasBounty(false);
    }

    // parse a line and create a player, or bounty, or mountain object and adds it to the board with its position
    private void loadData() throws ReaderException, GameException {
        int index = 1;

        // MANDATORY : find and assign map width and height
        for (String element : _reader.getLines()) {
            String[] tokens = element.replaceAll("\\s+","").split("-");
            if (tokens.length > 0) {
                processMapInfo(tokens, index);
            }
        }
        initBoardSize();

        // parse a line and identify if its a bounty, adventurer or mountain
        for (String element : _reader.getLines()) {
            String[] tokens = element.replaceAll("\\s+","").split("-");
            if (tokens.length > 0) {
                processLine(tokens, index);
            }
            index++;
        }
    }

    private void processMapInfo(String[] tokens, int index) throws ReaderException {
        String firstTokenUpper = tokens[0].toUpperCase();

        if (firstTokenUpper.equals("C")) {
            logger.log(Level.INFO, "Line {0}: MAP detected", index);
            tmpPosition = _reader.handleBoardPosition(tokens);
        }
    }

    private void processLine(String[] tokens, int index) throws ReaderException {
        String firstTokenUpper = tokens[0].toUpperCase();

        if (firstTokenUpper.equals("#")) {
            logger.log(Level.INFO, "Line {0}: COMMENTARY detected, passing", index);
        } else if (firstTokenUpper.equals("M")) {
            logger.log(Level.INFO, "Line {0}: MOUNTAIN detected", index);
            processMountain(tokens);
        } else if (firstTokenUpper.equals("T")) {
            logger.log(Level.INFO, "Line {0}: BOUNTY detected", index);
            processBounty(tokens);
        } else if (firstTokenUpper.equals("A")) {
            logger.log(Level.INFO, "Line {0}: ADVENTURER detected", index);
            processAdventurer(tokens);
        } else {
            logger.log(Level.INFO, "Line {0}: nothing detected, passing", index);
        }
    }

    private void processAdventurer(String[] tokens) throws ReaderException {
        Player tmpAdventurer = _reader.handleAdventurer(tokens);
        if (tmpAdventurer != null)
            _board.countPlayer();
        Position adventurerPos = new Position(tmpAdventurer.get_position().get_posX(), tmpAdventurer.get_position().get_posY());

        //check if there is at least one free cell or one Bounty available in the map
        if (_check.isEnoughSpaceOnBoard(_board.get_width(), _board.get_height(), _board.get_playersOnBoardCount(),
                _board.get_mountainsOnBoardCount())
                && _check.isElementPosIsValid(_board.get_width(), _board.get_height(), adventurerPos)) {
            // if the desired position is already occupied
            if (_board.get_elemsInBoard().containsKey(adventurerPos)) {
                CellInfo currentInfo = _board.get_elemsInBoard().get(adventurerPos);
                // if it is a mountain, we cannot stack a mountain and an adventurer
                if (currentInfo.is_hasMountain()) {
                    // we need to find a usable cell
                    // we look for either a free cell or a bounty cell near the our initial position
                    PositionResponse newPosition = _board.getEmptyPosition(adventurerPos, true, ElementType.BOUNTY);
                    if (newPosition != null) {
                        // if it is a free cell
                        // update the current cell by adding the adventurer
                        if (newPosition.is_isFreeCell()) {
                            CellInfo cellInfo = new CellInfo();
                            cellInfo.setNewAdventurer(tmpAdventurer);
                            logger.log(Level.INFO, "new Player {0} added from personnalized empty cell at {1}{2}",
                                    new Object[] {tmpAdventurer.get_name(), adventurerPos.get_posX(), adventurerPos.get_posY()});
                            _board.addElemOnBoard(newPosition, cellInfo);
                            _board.getPlayerList().add(tmpAdventurer);

                        } else {
                            //lookup the desired new cell and update it by adding the adventurer
                            CellInfo foundInfo = _board.get_elemsInBoard().get(newPosition);
                            currentInfo.setAdventurer(tmpAdventurer);
                            _board.addElemOnBoard(newPosition, foundInfo);
                            _board.getPlayerList().add(tmpAdventurer);
                            logger.log(Level.INFO, "new Player {0} added from personnalized occupied cell at {1}{2}",
                                    new Object[] {tmpAdventurer.get_name(), adventurerPos.get_posX(), adventurerPos.get_posY()});
                        }
                    }
                } else if (currentInfo.is_hasBounty()) { // if the cell is a bounty
                    currentInfo.setAdventurer(tmpAdventurer);
                    _board.get_elemsInBoard().replace(adventurerPos, currentInfo);
                    _board.getPlayerList().add(tmpAdventurer);
                    logger.log(Level.INFO, "new Player {0} added from occupied cell at {1}{2}",
                            new Object[] {tmpAdventurer.get_name(), adventurerPos.get_posX(), adventurerPos.get_posY()});
                }
            } else {
                CellInfo cellInfo = new CellInfo();
                cellInfo.setNewAdventurer(tmpAdventurer);
                _board.addElemOnBoard(adventurerPos, cellInfo);
                _board.getPlayerList().add(tmpAdventurer);
                logger.log(Level.INFO, "new Player {0} added at {1}{2}", new Object[] {tmpAdventurer.get_name(),
                        adventurerPos.get_posX(), adventurerPos.get_posY()});
            }
        }
    }

    private void processMountain(String[] tokens) throws ReaderException {
        Mountain tmpMountain = _reader.handleMountain(tokens);
        if (tmpMountain != null)
            _board.countMountain();
        if (_check.isEnoughSpaceOnBoard(_board.get_width(), _board.get_height(), _board.get_playersOnBoardCount(),
                _board.get_mountainsOnBoardCount())
                && _check.isElementPosIsValid(_board.get_width(), _board.get_height(), tmpMountain)) {
            Position pos = new Position(tmpMountain.get_posX(), tmpMountain.get_posY());
            if (_board.get_elemsInBoard().containsKey(pos)) {
                //check if superposable or move element near
                CellInfo currentInfo = _board.get_elemsInBoard().get(pos);
                if (currentInfo.is_hasPlayer() || currentInfo.is_hasBounty()) {
                    PositionResponse newPosition = _board.getEmptyPosition(pos, false, null);
                    if (newPosition != null) {
                        CellInfo cellInfo = new CellInfo();
                        cellInfo.setNewMountain();

                        _board.addElemOnBoard(newPosition, cellInfo);
                        logger.log(Level.INFO, "Mountain added at {0}{1} from personnalized position",
                                new Object[] {newPosition.get_posX(), newPosition.get_posY()});
                    }
                }
            } else {
                CellInfo cellInfo = new CellInfo();
                cellInfo.setNewMountain();

                _board.addElemOnBoard(pos, cellInfo);
                logger.log(Level.INFO, "Mountain added at {0}{1}",
                        new Object[] {pos.get_posX(), pos.get_posY()});
            }
        }
    }

    private void processBounty(String[] tokens) throws ReaderException {
        Bounty tmpBounty = _reader.handleBounty(tokens);
        if (tmpBounty != null && tmpBounty.get_bountyCount() == 0)
            return;
        if (_check.isEnoughSpaceOnBoard(_board.get_width(), _board.get_height(), _board.get_playersOnBoardCount(),
                _board.get_mountainsOnBoardCount())
                && _check.isElementPosIsValid(_board.get_width(), _board.get_height(), tmpBounty)) {
            Position pos = new Position(tmpBounty.get_posX(), tmpBounty.get_posY());
            if (_board.get_elemsInBoard().containsKey(pos)) {
                //check if superposable or move element near
                CellInfo currentInfo = _board.get_elemsInBoard().get(pos);
                if (currentInfo.is_hasMountain()) {
                    PositionResponse newPosition = _board.getEmptyPosition(pos, true, ElementType.ADVENTURER);
                    if (newPosition != null) {
                        if (newPosition.is_isFreeCell()) {
                            CellInfo cellInfo = new CellInfo();
                            cellInfo.setNewBounty(tmpBounty.get_bountyCount());
                            logger.log(Level.INFO, "{0} bounties added from personnalized empty cell at {1}{2}",
                                    new Object[] {tmpBounty.get_bountyCount(), newPosition.get_posX(),
                                            newPosition.get_posY()});

                            _board.addElemOnBoard(newPosition, cellInfo);
                        } else {
                            CellInfo foundInfo = _board.get_elemsInBoard().get(newPosition);
                            logger.log(Level.INFO, "{0} Amore bounties added from {1} at {2}{3}",
                                    new Object[] {tmpBounty.get_bountyCount(), foundInfo.get_bounties(),
                                            newPosition.get_posX(), newPosition.get_posY()});
                            foundInfo.setBounty(foundInfo.get_bounties() + tmpBounty.get_bountyCount());

                            _board.addElemOnBoard(newPosition, foundInfo);
                        }

                    }
                } else if (currentInfo.is_hasBounty() || currentInfo.is_hasPlayer()) {
                    logger.log(Level.INFO, "{0} Bmore bounties added from {1} at {2}{3}",
                            new Object[] {tmpBounty.get_bountyCount(), currentInfo.get_bounties(),
                                    pos.get_posX(), pos.get_posY()});
                    currentInfo.setBounty(currentInfo.get_bounties() + tmpBounty.get_bountyCount());
                    _board.get_elemsInBoard().replace(pos, currentInfo);
                }
            } else {
                CellInfo cellInfo = new CellInfo();
                cellInfo.setNewBounty(tmpBounty.get_bountyCount());
                _board.addElemOnBoard(pos, cellInfo);

                logger.log(Level.INFO, "{0} bounties added at {1}{2}", new Object[] {tmpBounty.get_bountyCount(),
                pos.get_posX(), pos.get_posY()});
            }
        }
    }
    private void initBoardSize() throws GameException {
        if (tmpPosition.get_posY() > 0 && tmpPosition.get_posX() > 0) {
            _board.set_height(tmpPosition.get_posY());
            _board.set_width(tmpPosition.get_posX());
        }
        if (_board.get_width() == 0 && _board.get_height() == 0)
            throw new GameException("No width nor height detected");
    }
}
