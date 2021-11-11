package Game.gameState;

import Game.gameState.interaction.item.ItemType;
import Game.gameState.posn.Posn;

/**
 * Hold a pair of object and position
 */
public class ObjectPosition {
    public ItemType type;
    public Posn position;

    /**
     * Constructor of the ObjectPosition.
     * @param type the type of the Objects.
     * @param position the position of the ObjectPosition.
     */
    public ObjectPosition(ItemType type, Posn position) {
        this.type = type;
        this.position = position;
    }

    public static ObjectPosition[] append(ObjectPosition[] original, ObjectPosition toAppend) {
        ObjectPosition[] result = new ObjectPosition[original.length + 1];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i];
        }
        result[original.length] = toAppend;
        return result;
    }
}
