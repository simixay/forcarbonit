package com.forcarbonit.reader;

import com.forcarbonit.game.Bounty;
import com.forcarbonit.game.Mountain;
import com.forcarbonit.game.Player;
import com.forcarbonit.game.Position;
import com.forcarbonit.exception.ReaderException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reader {
    private static final Logger logger = Logger.getLogger(ReaderException.class.getName());


    @Getter
    @Setter
    private List<String> lines = new ArrayList<>();

    // read and store each line of a file
    public void loadFile(String path) throws ReaderException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(path));
            while(scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            logger.log(Level.INFO, "Lines found out of file = {0}", lines);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ReaderException("Parsing file error");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    //[M:String, X:String, Y:String]
    //M: mountain designation, X: horizontal position, Y: Vertical Position
    public Mountain handleMountain(String[] tokens) throws ReaderException {
        if (tokens.length != 3 || !StringUtils.isNumeric(tokens[1]) || !StringUtils.isNumeric(tokens[2])) {
            throw new ReaderException("Mountain parsing error");
        }
        return new Mountain(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
    }

    //[BountyDesignation:String, X:String, Y:String, BountyCount:String]
    public Bounty handleBounty(String[] tokens) throws ReaderException {
        if (tokens.length != 4 || !StringUtils.isNumeric(tokens[1]) || !StringUtils.isNumeric(tokens[2])
                || !StringUtils.isNumeric(tokens[3])) {
            throw new ReaderException("Bounty parsing error");
        }
        return new Bounty(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
    }

    //[AdventurerDesignation:String, Name:String, X:String, Y:String, StartingDirection:String, Moves: Direction]
    public Player handleAdventurer(String[] tokens) throws ReaderException {
        StringBuilder tmpMoves = new StringBuilder();
        if (tokens.length != 6 || !StringUtils.isNumeric(tokens[2]) || !StringUtils.isNumeric(tokens[3])
                || !tokens[4].toUpperCase().matches("S|N|E|O")
                ) {
            throw new ReaderException("Adventurer parsing error");
        }
        for (char c : tokens[5].toCharArray()) {
            if (c == 'A' || c == 'G' || c == 'D') {
                tmpMoves.append(String.valueOf(c).toUpperCase());
            }
        }
        return new Player(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), tokens[4].charAt(0),
                tmpMoves.toString());
    }

    // C - 5 - 2
    // C:Map designation, 5: width, 2: height
    public Position handleBoardPosition(String[] tokens) throws ReaderException {
        if (tokens.length != 3 || !StringUtils.isNumeric(tokens[1]) || !StringUtils.isNumeric(tokens[2])) {
            throw new ReaderException("Board parsing error");
        }
        return new Position(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
    }
}
