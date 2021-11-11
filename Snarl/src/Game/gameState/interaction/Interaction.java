package Game.gameState.interaction;

import Game.gameState.level.Level;
import Game.gameState.entities.Entity;
import Game.gameState.GameState;

/**
 * Provide framework for interactions to follow visitor pattern
 * This will allow for changes to be propagate throughout the entire system and affect different types differently
 */
public interface Interaction {
    MoveResult getMoveResult();

    void visitState(GameState state);

    void visitLevel(Level level);

    void visitEntity(Entity entity);
}
