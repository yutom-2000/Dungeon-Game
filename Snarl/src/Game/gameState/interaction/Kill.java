package Game.gameState.interaction;

import Game.gameState.entities.EntityStatus;
import Game.gameState.level.Level;
import Game.gameState.entities.Entity;
import Game.gameState.GameState;

/**
 * Kill an entity
 * For now reserved for interaction when an adversary crosses paths with a player
 */
public class Kill extends AInteraction {
    private Entity subject; //The entity DOING the killing
    private Entity predicate; //The entity getting killed

    public Kill(Entity subject, Entity predicate) {
        super(MoveResult.EJECT);
        this.subject = subject;
        this.predicate = predicate;

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
        predicate.setStatus(EntityStatus.DEAD);
    }
}
