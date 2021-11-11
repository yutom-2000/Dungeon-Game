package Game.gameState.level.tiles;

import Game.gameState.interaction.*;
import Game.gameState.entities.Entity;
import Game.gameState.interaction.item.ItemType;

/**
 * Interface representing tiles in a Game.gameState.level
 */
public class Tile {

  Entity entity;
  ItemType item;
  TileType tileType;

  public Tile(TileType type) {
    this.item = ItemType.NONE;
    this.tileType = type;
  }

  /**
   * Return if this tile is a steppable tile, i.e. if an entity can move onto it RULES: 1. If the
   * tile has no entity, is steppable 2. If the tile has an entity, it cannot be on the same team as
   * the step-er
   *
   * @return true if entity can step onto tile and interact with it (not blocked by same-team
   * entity)
   */
  public boolean isStepable() {
    return tileType == TileType.FLOOR || tileType == TileType.DOOR || tileType == TileType.HALLWAY;
  }


  /**
   * RULES: Adversary: *  - zombie: *      cannot move on the door/wall (so it cannot move outside
   * the room.) *  - ghost: *      can move on to the wall, then randomly assign to new position
   *
   * @param adversary adversary in the game (can be ghost or zombie)
   * @return
   */
  public boolean isStepableAdversary(Entity adversary) {
    boolean notOccupied = !(this.entity != null && this.entity.isAdversary());
    boolean isStepable = notOccupied;

    if (adversary.isZombie()) {
      boolean notDoorTile = !this.isDoorTile();
      boolean notWallTile = !this.isWallTile();
      isStepable = notOccupied && notDoorTile && notWallTile;
    }
    return isStepable;
  }

  /**
   * Determine if is doorTile
   *
   * @return
   */
  public boolean isDoorTile() {
    return tileType == TileType.DOOR;
  }

  /**
   * Determine if is floor tile
   *
   * @return
   */
  public boolean isFloorTile() {
    return tileType == TileType.FLOOR;
  }

  /**
   * Determine if is wall tile
   *
   * @return
   */
  public boolean isWallTile() {
    return tileType == TileType.WALL;
  }

  /**
   * Determine if is hallway tile
   *
   * @return
   */
  public boolean isHallwayTile() {
    return tileType == TileType.HALLWAY;
  }

  /**
   * Place an entity on a tile Useful for initialization of the game, as well as facilitate
   * movement.
   *
   * @param entity    the entity to place on this tile
   * @param isInitial determine whether the entity is actived in the game
   * @return Tile with Entity
   */
  public Tile placeEntity(Entity entity, boolean isInitial) {
    if (isInitial) {
      if (this.isFloorTile() && this.entity == null && this.getItem() == ItemType.NONE) {
        this.entity = entity;
      } else {
        String type = "Player";
        if (entity.isAdversary()) {
          type = "Adversary";
        }
        String initial = "";
        if (isInitial) {
          initial = "Initial ";
        }
        System.out.println(
            "Illegal " + initial + "Placement: " + type + " " + entity.getName() + " on " + this
                .toString());
        throw new IllegalArgumentException(
            "Cannot place entity on non floor tile during initialization");
      }
    } else {
      if (this.entity == null) {
        this.entity = entity;
      }
    }
    return this;
  }

  /**
   * Convenience to not break older tests
   *
   * @param type the type of items
   * @return the Tile with the items on it
   */
  public Tile placeItem(ItemType type) {
    return placeItem(type, true);
  }

  /**
   * Initially place an item on a tile
   *
   * @param type the item to place on the tile
   * @return the Tile with the items on it
   */
  public Tile placeItem(ItemType type, boolean initial) {
    if (initial) {
      if (this.isFloorTile() && this.item == ItemType.NONE) {
        this.item = type;
      } else {
        throw new IllegalArgumentException(
            "Cannot place an item on non-floor tile or tile already containing item");
      }
    } else {
      this.item = type;
    }
    return this;
  }

  /**
   * Remove an entity from a tile (i.e. movement or elimination)
   */
  public Tile removeEntity() {
    this.entity = null;
    return this;
  }

  /**
   * Have the entity interact with the tile
   *
   * @param entity the entity on the tile
   * @return the Interaction objects
   */
  public Interaction getInteraction(Entity entity) {
    if (this.entity != null) {
      if (entity.isPlayer() && this.entity.isAdversary()) {
        return new Kill(this.entity, entity);
      }
      if (this.entity.isPlayer() && entity.isAdversary()) {
        return new Kill(entity, this.entity);
      }
    }
    if (entity.isPlayer()) {
      if (this.getItem() == ItemType.KEY) {
        return new UnlockExit(entity, this);
      }
      if (this.getItem() == ItemType.EXIT) {
        return new ExitLevel(entity);
      } else {
        return new NoInteraction();
      }
    }
    if (entity.isGhost() && this.isWallTile()) {
      return new RandomPlaceInteraction(entity);
    } else {
      return new NoInteraction();
    }
  }

  /**
   * Return the integer representing this tile
   *
   * @return the integer signifier the type of the tiles
   */
  public int toLayout() {
    switch (tileType) {
      case WALL:
        return 0;
      case DOOR:
        return 2;
      case FLOOR:
      case HALLWAY:
        return 1;
    }
    return -1; //shouldn't get here
  }

  /**
   * Get the item type stored on this tile.
   *
   * @return ItemType of types in the tile.
   */
  public ItemType getItem() {
    return this.item;
  }

  /**
   * Get the entity stored on this tile. Can be null.
   *
   * @return the Entity on the tile,
   */
  public Entity getEntity() {
    return this.entity;
  }

  /**
   * Get the type of this tile
   *
   * @return the tile type
   */
  public TileType getTileType() {
    return this.tileType;
  }


  @Override
  public String toString() {
    if (entity != null) {
      if (entity.isPlayer()) {
        return "P";
      } else {
        return "A";
      }
    }
    switch (tileType) {
      case DOOR:
        return "d";
      case WALL:
        return "■";
      case FLOOR:
        return item.toString();
      case HALLWAY:
        return "h";
    }
    return "error";
  }

  public static Tile[] append(Tile[] original, Tile toAppend) {
    Tile[] result = new Tile[original.length + 1];
    for (int i = 0; i < original.length; i++) {
      result[i] = original[i];
    }
    result[original.length] = toAppend;
    return result;
  }

  public static Tile[][] append(Tile[][] original, Tile[] toAppend) {
    Tile[][] result = new Tile[original.length + 1][];
    for (int i = 0; i < original.length; i++) {
      result[i] = original[i];
    }
    result[original.length] = toAppend;
    return result;
  }
}
