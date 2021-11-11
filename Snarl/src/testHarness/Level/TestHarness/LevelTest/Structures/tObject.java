package testHarness.Level.TestHarness.LevelTest.Structures;

import Game.gameState.posn.Posn;
import testHarness.Level.TestHarness.LevelTest.Enums.TileObjectEnum;

/**
 * Class describes an object and it's location
 */
public class tObject {
    TileObjectEnum type;
    Posn position;

    /**
     * Constructs a tObject
     * @param type
     * @param position
     */
    public tObject(TileObjectEnum type, Posn position) {
        this.type = type;
        this.position = position;
    }

    public Posn getPosition() {
        return this.position;
    }

    public TileObjectEnum getType() {
        return this.type;
    }
}
