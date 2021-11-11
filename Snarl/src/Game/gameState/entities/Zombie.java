package Game.gameState.entities;

import Game.gameState.interaction.Interaction;
import Game.gameState.level.Level;
import Game.gameState.level.tiles.Tile;
import Game.gameState.level.tiles.TileType;
import Game.gameState.posn.Posn;

import java.util.ArrayList;

public class Zombie extends Adversary {

  private int MAXMOVES = 1;

  /**
   * Primary constructor for the Ghost registration
   *
   * @param name the primary name of the adversary
   */
  public Zombie(String name) {
    super(name, EntityType.ZOMBIE, null, EntityStatus.ALIVE);
  }

  public Zombie(String name, Posn position) {
    super(name, EntityType.ZOMBIE, position, EntityStatus.ALIVE);
  }

  public Zombie(int adversaryId) {
    super("zombie" + adversaryId, EntityType.ZOMBIE, null, EntityStatus.ALIVE);
  }

  @Override
  public int getMaxMoves() {
    return this.MAXMOVES;
  }

  /**
   * Zombies can step on a tile if it 1. Does not contain an adversary 2. Does not contain an item
   * 3. Is of type floor
   *
   * @param t
   * @return
   */
  @Override
  public boolean canStep(Tile t) {
    return (t.getEntity() == null || !t.getEntity().isAdversary())
        && t.getTileType() == TileType.FLOOR;
  }

}
