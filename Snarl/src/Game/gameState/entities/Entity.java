package Game.gameState.entities;

import Game.gameState.ActorPosition;
import Game.gameState.interaction.Interaction;
import Game.gameState.interaction.InteractionAccept;
import Game.gameState.level.tiles.Tile;
import Game.gameState.posn.Posn;

/**
 * A class represent the Entity
 */
public abstract class Entity implements InteractionAccept {
  String name;
  EntityType type;
  Posn position;
  EntityStatus status;

  public Entity(String name, EntityType type, Posn position, EntityStatus status) {
    this.name = name;
    this.type = type;
    this.position = position;
    this.status = status;
  }

  /**
   * Determine team affiliation with players
   * @return
   */
  public boolean isPlayer() {
    return type == EntityType.PLAYER;
  }

  /**
   * Determine team affiliation with adversaries
   * @return
   */
  public boolean isAdversary() {
    return isZombie() || isGhost();
  }

  public boolean isZombie() {
    return type == EntityType.ZOMBIE;
  }
  public boolean isGhost() {
    return type == EntityType.GHOST;
  }

  /**
   * Get player's or Adversary's name
   *
   * @return the name of the player
   */
  public String getName() {
    return name;
  }

  /**
   * Get the position of the entity
   * @return
   */
  public Posn getPosn() {
    return position;
  }

  /**
   * Set the position of this entity
   * @param p
   */
  public void setPosn(Posn p) {
    position = p;
  }

  /**
   * Get an entity's status (alive, dead, exited level)
   *
   * @return
   */
  public EntityStatus getStatus() {
    return status;
  }

  /**
   * Set an entity's status
   */
  public void setStatus(EntityStatus status) {
    this.status = status;
  }

  /**
   * Get the maximum number of moves allowed
   * @return
   */
  public abstract int getMaxMoves();

  /**
   * Determine if a tile can be stepped on by this entity
   */
  public abstract boolean canStep(Tile t);

  /**
   * Get this entity's type
   * @return
   */
  public EntityType getType() {
    return this.type;
  }

  @Override
  public void accept(Interaction interaction) {
    interaction.visitEntity(this);
  }

  /**
   * Create an actorPosition from this entity
   * @return
   */
  public ActorPosition toActorPosition() {
    return new ActorPosition(type, name, position);
  }
}
