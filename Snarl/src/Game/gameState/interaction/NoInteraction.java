package Game.gameState.interaction;

import Game.gameState.level.Level;
import Game.gameState.entities.Entity;
import Game.gameState.GameState;

/**
 * Represent no interaction.
 */
public class NoInteraction extends AInteraction {

    public NoInteraction() {
        super(MoveResult.OK);
    }
    @Override
    public void visitState(GameState state) {

    }

    @Override
    public void visitLevel(Level level) {

    }

    @Override
    public void visitEntity(Entity entity) {

    }
}
