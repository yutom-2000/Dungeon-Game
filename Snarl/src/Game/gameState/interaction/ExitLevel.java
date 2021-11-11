package Game.gameState.interaction;

import Game.gameState.GameState;
import Game.gameState.entities.Entity;
import Game.gameState.entities.EntityStatus;
import Game.gameState.level.Level;

/**
 * Function object for a player to leave the level
 */
public class ExitLevel extends AInteraction {
    Entity entity;

    /**
     * Create new ExitLevel function object
     * @param entity the entity that stepped upon the exit tile
     */
    public ExitLevel (Entity entity) {
        super(MoveResult.EXIT);
        this.entity = entity;
    }


    @Override
    public void visitState(GameState state) {
        if (!(state.getIsLocked()) && entity.isPlayer()) { //if unlocked exit & interactor is player
            state.setIsExit(true);
            entity.setStatus(EntityStatus.EXITED);
        }
    }

    @Override
    public void visitLevel(Level level) {
        // do nothing
    }

    @Override
    public void visitEntity(Entity entity) {
        //if the entity accepting interaction is not the entity that walked onto the exit tile, don't do anything
    }
}
