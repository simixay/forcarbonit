package com.forcarbonit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import com.forcarbonit.exception.GameException;
import com.forcarbonit.exception.ReaderException;
import com.forcarbonit.game.JumanjiGame;
import com.forcarbonit.reader.Reader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

public class GameTest {
    String cwd = System.getProperty("user.dir");
    String testPath = cwd + "/src/com/forcarbonit/test/";

    @Test(expected = ReaderException.class)
    public void testInvalidOrientation() throws ReaderException, GameException {
        String filePath = testPath + "invalid_orientation.txt";
        JumanjiGame jumanjiGame = new JumanjiGame();
        jumanjiGame.play(filePath);
    }

    @Test
    public void testFromPdf() throws ReaderException, GameException {
        String ret = testNormal("from_pdf.txt");

        StringBuilder sb = new StringBuilder();
        sb.append("C - 3 - 4\n")
                .append("M - 1 - 0\n")
                .append("M - 2 - 1\n")
                .append("# {A comme Aventurier} - {Nom de l'aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}\n")
                .append("A - Lara - 0 - 3 - S - 3\n")
                .append("# {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}\n")
                .append("T - 1 - 3 - 2\n");
        assertEquals("should be equal", ret, sb.toString());
    }

    @Test
    public void testMapFullPlayers() throws ReaderException, GameException {
        String ret = testNormal("map_full_players.txt");
        StringBuilder sb = new StringBuilder();
        sb.append("C - 2 - 2\n")
                .append("# {A comme Aventurier} - {Nom de l'aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}\n")
                .append("A - Patrick - 0 - 1 - E - 0\n")
                .append("# {A comme Aventurier} - {Nom de l'aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}\n")
                .append("A - Sim - 1 - 0 - E - 0\n")
                .append("# {A comme Aventurier} - {Nom de l'aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}\n")
                .append("A - Xavier - 1 - 1 - E - 0\n")
                .append("# {A comme Aventurier} - {Nom de l'aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}\n")
                .append("A - Lara - 0 - 0 - E - 0\n");
        assertEquals("should be equal", ret, sb.toString());
    }

    @Test
    public void testMapLimitation() throws ReaderException, GameException {
        String ret = testNormal("map_limitation.txt");
        StringBuilder sb = new StringBuilder();

        sb.append("C - 2 - 2\n")
                .append("# {A comme Aventurier} - {Nom de l'aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}\n")
                .append("A - Lara - 0 - 1 - N - 0\n");
        assertEquals("should be equal", ret, sb.toString());
    }

    @Test
    public void testSpawnOnBounty() throws ReaderException, GameException {
        String ret = testNormal("spawn_on_bounty.txt");
        StringBuilder sb = new StringBuilder();

        sb.append("C - 2 - 2\n")
                .append("# {A comme Aventurier} - {Nom de l'aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}\n")
                .append("A - Lara - 0 - 1 - N - 1\n")
                .append("# {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}\n")
                .append("T - 0 - 0 - 4\n");
        assertEquals("should be equal", ret, sb.toString());
    }

    @Test
    public void testSurroundedByMountains() throws ReaderException, GameException {
        String ret = testNormal("surrounded_by_mountains.txt");
        StringBuilder sb = new StringBuilder();

        sb.append("C - 2 - 2\n")
                .append("M - 0 - 1\n")
                .append("M - 1 - 0\n")
                .append("# {A comme Aventurier} - {Nom de l'aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}\n")
                .append("A - Sim - 0 - 0 - E - 0\n");
        assertEquals("should be equal", ret, sb.toString());
    }


    @Test
    public void testMoves() throws ReaderException, GameException {
        String ret = testNormal("moves_test.txt");
        StringBuilder sb = new StringBuilder();

        sb.append("C - 2 - 2\n")
                .append("# {A comme Aventurier} - {Nom de l'aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}\n")
                .append("A - marie - 0 - 0 - S - 0\n");
        assertEquals("should be equal", ret, sb.toString());
    }

    @Test(expected = GameException.class)
    public void testWithoutMap() throws ReaderException, GameException {
        String filePath = testPath + "without_map.txt";
        JumanjiGame jumanjiGame = new JumanjiGame();
        jumanjiGame.play(filePath);
    }
    public String testNormal(String str) throws ReaderException, GameException {
        String filePath = testPath + str;
        JumanjiGame jumanjiGame = new JumanjiGame();
        jumanjiGame.play(filePath);

        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        // Print some output: goes to your special stream
        jumanjiGame.get_board().printGamePrompt();
        // Put things back
        System.out.flush();
        System.setOut(old);
        // Show what happened
        return baos.toString();
    }

}