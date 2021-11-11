package Game.gameState.interaction.item;

/**
 * Represent the different types of placeables
 * NONE: represents state of having no modifier (no interaction)
 * KEY: represents state of holding a key
 * EXIT: represents state of holding an exit
 */
public enum ItemType {
    NONE, KEY, EXIT;

    @Override
    public String toString() {
        switch (this) {
            case NONE:
                return "□";
            case KEY:
                return "K";
            case EXIT:
                return "E";
        }
        return ""; //should never reach here
    }
}


