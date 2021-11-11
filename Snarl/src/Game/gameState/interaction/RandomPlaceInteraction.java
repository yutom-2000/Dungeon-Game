package Game.gameState.interaction;

import Game.gameState.GameState;
import Game.gameState.entities.Entity;
import Game.gameState.entities.EntityStatus;
import Game.gameState.entities.Ghost;
import Game.gameState.level.Level;

import java.util.Random;

/**
 * A class for interaction which randomly put the entity on the level.
 */
public class RandomPlaceInteraction extends AInteraction{
    Entity ghost;

    public RandomPlaceInteraction(Entity entity) {
        super(MoveResult.OK);
        this.ghost = entity;
    }

    @Override
    public void visitState(GameState state) {
        //do nothing
    }

    @Override
    public void visitLevel(Level level) {
        //do nothing
    }

    @Override
    public void visitEntity(Entity entity) {
        entity.setStatus(EntityStatus.TELEPORT); //flag this entity for teleport for later use
    }
}
