package Game.gameState.interaction;

/**
 * Interface for all classes that can accept an interaction
 *
 * Used to propagate change throughout entire gamestate accordingly
 */
public interface InteractionAccept {
    void accept(Interaction interaction);
}
