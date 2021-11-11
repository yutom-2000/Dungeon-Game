package Game.gameState.interaction;

import Game.gameState.entities.Player;
import Game.gameState.interaction.item.ItemType;
import Game.gameState.level.Level;
import Game.gameState.entities.Entity;
import Game.gameState.GameState;
import Game.gameState.level.tiles.Tile;

/**
 * Unlock this level's exit
 */
public class UnlockExit extends AInteraction {
    Player unlocker;
    Tile keyTile;

    public UnlockExit(Entity unlocker, Tile keyTile) {
        super(MoveResult.KEY);
        this.unlocker = (Player) unlocker;
        this.keyTile = keyTile;
    }

    @Override
    public void visitState(GameState state) {
        state.unlockExit();
        state.reInteract();
    }

    @Override
    public void visitLevel(Level level) {
        keyTile.placeItem(ItemType.NONE, false);
    }

    @Override
    public void visitEntity(Entity entity) {
        unlocker.setKeyHolder(true);
    }
}
