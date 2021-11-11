package testHarness.Level.TestHarness.LevelTest;

import Game.gameState.posn.Posn;
import testHarness.Level.TestHarness.LevelTest.Enums.TileLocationType;
import testHarness.Level.TestHarness.LevelTest.Enums.TileObjectEnum;

public class LevelTestHarnessResult {
    boolean traversable;
    String object;
    String type;
    int[][] reachable;

    public LevelTestHarnessResult(boolean t, TileObjectEnum obj, TileLocationType type, Posn[] reachable) {
        this.traversable = t;

        switch (obj) {
            case KEY:
                this.object = "key";
                break;
            case EXIT:
                this.object = "exit";
                break;
            case NONE:
                this.object = null;
                break;
        }

        switch (type) {
            case ROOM:
                this.type = "room";
                break;
            case HALLWAY:
                this.type = "hallway";
                break;
            case VOID:
                this.type = "void";
                break;
        }

        this.reachable = new int[reachable.length][];
        for (int i = 0; i < reachable.length; i++) {
            this.reachable[i] = reachable[i].toArray();
        }

    }

}
