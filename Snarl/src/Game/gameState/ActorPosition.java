package Game.gameState;

import Game.gameState.entities.EntityType;
import Game.gameState.posn.Posn;

/**
 * For actor-position-list
 */
public class ActorPosition {
    public EntityType type;
    public String name; //name of the actor
    public Posn position;

    /**
     * Construction for ActorPosition
     * @param type types of the entity (we have three types)
     * @param name the name of the Entity
     * @param position the position of the given entity.
     */
    public ActorPosition(EntityType type, String name, Posn position) {
        this.type = type;
        this.name = name;
        this.position = position;
    }

    /**
     * Append the position to existed actorPosition lists
     * @param original the original array of actorPosition
     * @param toAppend the new position need to add to the arrays
     * @return the arraylist of the actor positions.
     */
    public static ActorPosition[] append(ActorPosition[] original, ActorPosition toAppend) {
        ActorPosition[] result = new ActorPosition[original.length + 1];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i];
        }
        result[original.length] = toAppend;
        return result;
    }

}
