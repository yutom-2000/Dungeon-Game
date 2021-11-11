package Game.gameState.interaction;

/**
 * Abstract class for interactions
 *
 */
public abstract class AInteraction implements Interaction {
    MoveResult result;

    public AInteraction(MoveResult result) {
        this.result = result;
    }

    @Override
    public MoveResult getMoveResult() {
        return result;
    }
}
