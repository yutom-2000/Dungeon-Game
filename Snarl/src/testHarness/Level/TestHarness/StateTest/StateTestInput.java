package testHarness.Level.TestHarness.StateTest;

import Game.gameState.GameState;
import Game.gameState.posn.Posn;

/**
 * Hold all input information from a single stateTest input
 */
public class StateTestInput {
    GameState gameState;
    String name;
    Posn position;

    public StateTestInput(GameState gameState, String name, Posn position) {
        this.gameState = gameState;
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return this.name;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public Posn getPosition() {
        return position;
    }
}
