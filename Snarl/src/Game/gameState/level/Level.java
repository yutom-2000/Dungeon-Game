package Game.gameState.level;

import java.util.ArrayList;
import java.util.Random;

import Game.gameState.ActorPosition;
import Game.gameState.ObjectPosition;
import Game.gameState.interaction.InteractionAccept;
import Game.gameState.interaction.Interaction;
import Game.gameState.entities.Entity;
import Game.gameState.level.generation.Hallway;
import Game.gameState.level.generation.Room;
import Game.gameState.interaction.item.Item;
import Game.gameState.interaction.item.ItemType;
import Game.gameState.level.tiles.*;
import Game.gameState.posn.Posn;

/**
 * Represents a single level - roomNumber: an identifier for the level
 * <p>
 * LEVEL CREATION ORDER: 1. Accept rooms and hallways to calculate tiles field 2. Then gamestate
 * will place players and adversaries onto the level
 * <p>
 * SHOULD NOT MODIFY ANY FIELDS OF ENTITIES Only hold entities as variables and pass them around and
 * such (i.e. change tiles it's placed on)
 */
public class Level implements InteractionAccept {

  Tile[][] tiles;
  Hallway[] hallways;
  Room[] rooms;

  /**
   * Constructor for a level
   *
   * @param rooms        list of rooms
   * @param hallways     list of hallways
   * @param levelObjects list of levelObjects (ItemType/Position pair)
   */
  public Level(Room[] rooms, Hallway[] hallways, Item[] levelObjects) {
    this.tiles = fillWallTile(getMaxColBound(rooms, hallways), getMaxRowBound(rooms, hallways));
    tiles = placeRooms(tiles, rooms);
    tiles = placeHallways(tiles, hallways);
    this.rooms = rooms;
    this.hallways = hallways;

    for (Item item : levelObjects) {
      placeItem(item);
    }
  }

  /**
   * Secondary constructor for Level
   *
   * @param rooms    list of rooms
   * @param hallways list of hallways
   * @param exitPosn the position of the EXIT
   */
  public Level(Room[] rooms, Hallway[] hallways, Posn exitPosn) {
    this.rooms = rooms;
    this.hallways = hallways;
    this.tiles = fillWallTile(getMaxColBound(rooms, hallways), getMaxRowBound(rooms, hallways));
    tiles = placeRooms(tiles, rooms);
    tiles = placeHallways(tiles, hallways);
    tiles[exitPosn.getRow()][exitPosn.getCol()] = new Tile(TileType.FLOOR).placeItem(ItemType.EXIT);
    this.tiles = tiles;
  }

  /**
   * Try to place entities in a room Used for initialization (top left for players, bottom right for
   * adversaries)
   *
   * @param room       the rooms where gonna place the entities.
   * @param entityList the list of entity.
   */
  public void placeEntityInRoom(Room room, Entity[] entityList) {
    if (room == null && entityList.length != 0) {
      throw new IllegalArgumentException("Can't place on null room");
    }
    Posn origin = room.getOrigin();
    int index = 0; //index for entity list
    for (int y = 0; y < room.getRows(); y++) {
      for (int x = 0; x < room.getColumns(); x++) {
        if (index == entityList.length) {
          return;
        } else {
          if (placeEntity(entityList[index], new Posn(origin.getRow() + y, origin.getCol() + x),
              true)) {
            index++;
          }
        }
      }
    }
  }

  /**
   * Get the top left room Interpreted as having lowest sum of square row and col
   *
   * @return the top left room in the level for placing the players.
   */
  public Room getTopLeftRoom() {
    double lowestSum = Math.pow(this.tiles.length, 2) + Math
        .pow(this.tiles[1].length, 2); //coordinate for bottom right
    Room topLeftRoom = null;
    for (Room room : rooms) {
      Posn origin = room.getOrigin();
      double distance = Math.pow(origin.getCol(), 2) + Math.pow(origin.getRow(), 2);
      if (distance <= lowestSum) {
        lowestSum = distance;
        topLeftRoom = room;
      }
    }
    return topLeftRoom;
  }

  /**
   * Get the bottom right room. Interpreted as having greatest sum of row and col.
   *
   * @return bottom right room for placing the adversary.
   */
  public Room getBottomRightRoom() {
    double highestSum = 0;
    Room bottomRightRoom = null;
    for (Room room : rooms) {
      Posn origin = room.getOrigin();
      double distance = Math.pow(origin.getRow(), 2) + Math.pow(origin.getCol(), 2);
      if (distance >= highestSum) {
        highestSum = distance;
        bottomRightRoom = room;
      }
    }
    return bottomRightRoom;
  }

  /**
   * Convenience method for place entity during move during game - Do not have to follow initial
   * placement rules
   *
   * @param entity entity need to be placed
   * @param dest   the tile accept the entity
   * @return true if entity was successfully placed
   */
  public boolean placeEntity(Entity entity, Posn dest) {
    return placeEntity(entity, dest, false);
  }

  /**
   * Place entity on a given tile at point p
   *
   * @param entity    entity need to be placed
   * @param dest      the tile accept the entity
   * @param isInitial whether or not need to check for initial placement rules
   * @return true if entity was successfully placed
   */
  public boolean placeEntity(Entity entity, Posn dest, boolean isInitial) {
    try {
      tileAt(dest).placeEntity(entity, isInitial);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Get the Tile at a given position
   *
   * @param p position of tile to get
   * @return the Tile at posn p
   * @throws IllegalArgumentException
   */
  public Tile tileAt(Posn p) throws IllegalArgumentException {
    if (p.getRow() < this.tiles.length && p.getCol() < this.tiles[0].length
        && p.getRow() >= 0 && p.getCol() >= 0) { //determine if valid posn within level
      return this.tiles[p.getRow()][p.getCol()];
    } else {
      throw new IndexOutOfBoundsException("Given posn is OOB");
    }
  }


  /**
   * Place Rooms onto tile grid
   *
   * @param tiles the tiles accept the room
   * @param rooms the array of rooms
   * @return assign the tile the room.
   */
  private Tile[][] placeRooms(Tile[][] tiles, Room[] rooms) {
    for (Room r : rooms) {
      int originRow = r.getOrigin().getRow();
      int originCol = r.getOrigin().getCol();
      for (int row = 0; row < r.getRows(); row++) {
        for (int col = 0; col < r.getColumns(); col++) {
          //if tiles at that spot is walkable, somebody else already placed something there and is bad
          if (this.tiles[originRow + row][originCol + col].isStepable()) {
            throw new IllegalArgumentException("Tried to place overlapping room");
          } else {
            tiles[originRow + row][originCol + col] = r.getTiles()[row][col];
          }
        }
      }
    }
    return tiles;
  }

  /**
   * Place hallways onto tile grid
   *
   * @param tiles the tile accept hallway
   * @param hallways the hallway arrays
   * @return assign the tile the hallways.
   */
  private Tile[][] placeHallways(Tile[][] tiles, Hallway[] hallways) {
    for (Hallway h : hallways) {

      Posn[] posns = h.getHallTilePosns();
      for (Posn p : posns) {
        if (this.tiles[p.getRow()][p.getCol()].isStepable()) {
          throw new IllegalArgumentException("Tried to place overlapping hallway");
        } else {
          tiles[p.getRow()][p.getCol()] = new Tile(TileType.HALLWAY);
        }
      }
    }
    return tiles;
  }

  /**
   * Get a random empty floor posn in a room (no item or entity)
   *
   * @return the position of empty floor
   */
  public Posn getRandomInRoom() {
    Posn result = new Posn((int) (Math.random() * tiles.length),
        (int) (Math.random() * tiles[0].length));
    Tile picked = tileAt(result);
    while (picked.getEntity() != null || picked.getItem() != ItemType.NONE
        || picked.getTileType() != TileType.FLOOR) {
      //if does not satistfy all 3 conditions, re-roll
      result = new Posn((int) (Math.random() * tiles.length),
          (int) (Math.random() * tiles[0].length));
      picked = tileAt(result);
    }
    return result;
  }

  //HELPER METHODS, for general the square of wall tiles around the hallway and rooms.
  /**
   * Get maximum X-coord (col number) of all Rooms and hallways
   *
   * @param rooms the array of rooms.
   * @param hallways the array of hallways.
   * @return maximum x-coord
   */
  private int getMaxColBound(Room[] rooms, Hallway[] hallways) {
    int xBound = 0;
    for (Room r : rooms) {
      xBound = Math.max(xBound, r.getOrigin().getCol() + r.getColumns());
    }
    for (Hallway h : hallways) {
      xBound = Math.max(xBound, h.getFrom().getCol());
      xBound = Math.max(xBound, h.getTo().getCol());
      for (Posn p : h.getWaypoints()) {
        xBound = Math.max(xBound, p.getCol());
      }
    }
    return xBound + 1; //add a border for wall tiles
  }

  /**
   * Get maximum X-coord (col number) of all Rooms and hallways
   *
   * @param rooms the array of rooms.
   * @param hallways the array of hallways.
   * @return maximum y-coord
   */
  private int getMaxRowBound(Room[] rooms, Hallway[] hallways) {
    int yBound = 0;
    for (Room r : rooms) {
      yBound = Math.max(yBound, r.getOrigin().getRow() + r.getRows());
    }
    for (Hallway h : hallways) {
      yBound = Math.max(yBound, h.getFrom().getRow());
      yBound = Math.max(yBound, h.getTo().getRow());
      for (Posn p : h.getWaypoints()) {
        yBound = Math.max(yBound, p.getRow());
      }
    }
    return yBound + 1; //add a border for wall tiles
  }


  private Tile[][] fillWallTile(int numCols, int numRows) {
    Tile[][] result = new Tile[numRows][numCols];
    for (int y = 0; y < numRows; y++) {
      for (int x = 0; x < numCols; x++) {
        result[y][x] = new Tile(TileType.WALL);
      }
    }
    return result;
  }

  public Tile[][] getTiles() {
    return this.tiles;
  }

  @Override
  public String toString() {
    String result = "";
    for (int y = 0; y < tiles.length; y++) {
      for (int x = 0; x < tiles[y].length; x++) {
        result += (tiles[y][x].toString());
      }
      result += ("\n");
    }
    return result;
  }

  /**
   * Place item at given posn
   *
   * @param item the item need to be placed
   */
  public void placeItem(Item item) {
    Posn pos = item.getPosition();
    this.tiles[pos.getRow()][pos.getCol()].placeItem(item.getType());
  }


  @Override
  public void accept(Interaction interaction) {
    interaction.visitLevel(this);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Level) {
      Level other = (Level) o;
      return this.toString().equals(other.toString());
    } else {
      return false;
    }
  }


  /**
   * Get a string representation of some square subset of the level as defined by the two posns
   * <p>
   * Will include the topleft tile, and exclude the row and col of bottomRight
   *
   * @param topLeft     top left corner of subset
   * @param bottomRight bottom right corner of subset
   * @return String representing the subset
   */
  public String getSubsetString(Posn topLeft, Posn bottomRight) {
    Posn indexPosn = new Posn(topLeft.getRow(), topLeft.getCol());
    String result = "";

    while (indexPosn.getRow() < bottomRight.getRow()) {
      while (indexPosn.getCol() < bottomRight.getCol()) {
        try {
          result += tileAt(indexPosn).toString();
          indexPosn.setCol(indexPosn.getCol() + 1);
        } catch (IndexOutOfBoundsException e) {
          result += new Tile(TileType.WALL).toString();
          indexPosn.setCol(indexPosn.getCol() + 1);
        }
      }
      indexPosn.setCol(topLeft.getCol());
      indexPosn.setRow(indexPosn.getRow() + 1);
      result += "\n";
    }
    return result;
  }

  /**
   * Get integer tileLayout for update message
   *
   * @param topLeft the position of topLeft of the room
   * @param bottomRight the position of bottomRight of the room
   * @return the integer tileLayout for json output.
   */
  public int[][] getTileLayout(Posn topLeft, Posn bottomRight) {
    Tile[][] tiles = getSubsetTile(topLeft, bottomRight);
    int[][] result = new int[tiles.length][tiles[0].length];

    int rowIter = 0;
    for (Tile[] row : tiles) {
      int colIter = 0;
      for (Tile tile : row) {
        result[rowIter][colIter] = tile.toLayout();
        colIter++;
      }
      rowIter++;
    }
    return result;
  }

  /**
   * Get the subset tile in the global view
   * (for reporting the view for adversary)
   * @param topLeft the position of topLeft of the room
   * @param bottomRight the position of bottomRight of the room
   * @return the tiles where in the bound of the room of topLeft and bottomRight.
   */
  public Tile[][] getSubsetTile(Posn topLeft, Posn bottomRight) {
    Tile[][] result = new Tile[][]{};
    int r = topLeft.getRow(), c = topLeft.getCol();
    for (int row = 0; row < bottomRight.getRow() - topLeft.getRow(); row++) {
      Tile[] rowTiles = new Tile[]{};
      for (int col = 0; col < bottomRight.getCol() - topLeft.getCol(); col++) {
        try {
          rowTiles = Tile.append(rowTiles, tileAt(new Posn(r + row, c + col)));
        } catch (IndexOutOfBoundsException e) {
          rowTiles = Tile.append(rowTiles, new Tile(TileType.WALL));
        }
      }
      result = Tile.append(result, rowTiles);
    }
    return result;
  }

  /**
   * Get actor position list from a subset of tiles
   * (For outputing gamestate)
   *
   * @param subsetTiles the subset Tiles
   * @return the position array of all entities.
   */
  public ActorPosition[] getActorPositionList(Tile[][] subsetTiles) {
    ActorPosition[] result = new ActorPosition[0];
    for (Tile[] row : subsetTiles) {
      for (Tile t : row) {
        Entity entity = t.getEntity();
        if (entity != null) {
          result = ActorPosition.append(result, t.getEntity().toActorPosition());
        }
      }
    }
    return result;
  }

  /**
   * Get objectPositionList from a subset of tiles and given topLeft position
   * (For outputing gamestate)
   *
   * @param subsetTiles the subset Tiles
   * @param topLeft the top left position of the level
   * @return the position array of all objects.
   */
  public ObjectPosition[] getObjectPositionList(Tile[][] subsetTiles, Posn topLeft) {
    ObjectPosition[] result = new ObjectPosition[0];
    for (int i = 0; i < subsetTiles.length; i++) {
      for (int j = 0; j < subsetTiles[0].length; j++) {
        ItemType item = subsetTiles[i][j].getItem();
        if (item != ItemType.NONE) {
          Posn itemPosn = new Posn(topLeft.getRow() + i, topLeft.getCol() + j);
          result = ObjectPosition.append(result, new ObjectPosition(item, itemPosn));
        }
      }
    }
    return result;
  }

  /**
   * Convenience method to get subset starting at topleft all the way to the bottom right corner of the
   * possible level
   *
   * @param topLeft the position of top left level
   * @return the string of all tiles in the level.
   */
  public String getSubsetString(Posn topLeft) {
    return getSubsetString(topLeft, new Posn(tiles.length, tiles[0].length));
  }

  /**
   * get the rooms on the level.
   * @return array rooms of the level.
   */
  public Room[] getRooms() {
    return this.rooms;
  }

}