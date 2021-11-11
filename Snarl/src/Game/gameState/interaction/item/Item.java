package Game.gameState.interaction.item;

import Game.gameState.posn.Posn;

/**
 * Represent a placeable object
 *  - type: type of placeable
 *  - position: where to place this placeable
 */
public class Item {
    ItemType type;
    Posn position;

    public Item(ItemType type, Posn position) {
        this.type = type;
        this.position = position;
    }

    public ItemType getType() {
        return this.type;
    }

    public Posn getPosition() {
        return this.position;
    }
}
