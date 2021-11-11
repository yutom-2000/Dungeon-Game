package Game.gameState.entities;

import Game.gameState.interaction.Interaction;
import Game.gameState.level.Level;
import Game.gameState.level.tiles.Tile;
import Game.gameState.posn.Posn;

import java.util.ArrayList;

public class Ghost extends Adversary {

  private int MAXMOVES = 1;

  /**
   * Primary constructor for the Ghost registration
   *
   * @param name the primary name of the adversary
   */
  public Ghost(String name) {
    super(name, EntityType.GHOST, null, EntityStatus.ALIVE);
  }

  public Ghost(int i) {
    super("ghost" + i, EntityType.GHOST, null, EntityStatus.ALIVE);
  }

  public Ghost(int i, Posn posn) {
    super("ghost" + i, EntityType.GHOST, posn, EntityStatus.ALIVE);
  }

  @Override
  public int getMaxMoves() {
    return MAXMOVES;
  }

  @Override
  public boolean canStep(Tile t) {
    return t.getEntity() == null || !t.getEntity().isAdversary();
  }
}
