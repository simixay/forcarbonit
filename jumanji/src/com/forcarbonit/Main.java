package com.forcarbonit;

import com.forcarbonit.exception.GameException;
import com.forcarbonit.exception.ReaderException;
import com.forcarbonit.game.JumanjiGame;

public class Main {

    public static void main(String[] args) {
        try {
            String cwd = System.getProperty("user.dir");
            String path = cwd + "/src/test.txt";
            JumanjiGame jumanjiGame = new JumanjiGame();
            jumanjiGame.play(path);
        } catch (ReaderException e) {
            e.printStackTrace();
        }
        catch (GameException e) {
            e.printStackTrace();
        }
    }
}
