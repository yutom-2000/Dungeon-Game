package Game.gameState.entities;

import Game.gameState.level.Level;
import Game.gameState.level.tiles.Tile;
import Game.gameState.level.tiles.TileType;
import Game.gameState.posn.Posn;
import java.util.ArrayList;

/**
 * An interface represent all players in the game.
 */
public abstract class Players extends Entity {

  public Players(String name, EntityType type, Posn position, EntityStatus status) {
    super(name, type, position, status);
  }

  /**
   * Players can step on a tile if it does not contain a player and the tile type is not wall
   *
   * @param t
   * @return
   */
  @Override
  public boolean canStep(Tile t) {
    return (t.getEntity() == null || !t.getEntity().isPlayer()) && t.getTileType() != TileType.WALL;
  }
}
