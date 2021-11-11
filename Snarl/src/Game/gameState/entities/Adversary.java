package Game.gameState.entities;

import Game.gameState.posn.Posn;

/**
 * The abstract class represent the all adversaries.
 */
public abstract class Adversary extends Entity {

    public Adversary(String name, EntityType type, Posn position, EntityStatus status) {
        super(name, type, position, status);
    }


}
